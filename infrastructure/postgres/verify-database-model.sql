-- infrastructure/postgres/verify-database-model.sql
--
-- Purpose:
--   Verify the database role, ownership, schema, and privilege model against
--   the postgres role model's claims — catalog-level, on demand.
--   (adopted from prior-work verification scripts; trimmed to this project's
--    two-role instantiation)
--
-- Run as:
--   the bootstrap identity (postgres), on demand:
--   podman exec -it idempotent-orders-postgres \
--     psql -U postgres -d idempotent_orders -f - < infrastructure/postgres/verify-database-model.sql
--
-- Database context:
--   idempotent_orders
--
-- Boundary:
--   Verifies the bootstrap model only — no application tables are required to
--   exist; runtime privileges on real tables are verified after real
--   migrations create them. The behavioral check (runtime DDL refusal) lives
--   in the manual beside this script.

\echo ''
\echo '=== 1. Project roles and role capabilities ==='
-- expected: both roles exist; no superuser, no createdb, no createrole; login only
SELECT
  rolname,
  rolsuper,
  rolcreatedb,
  rolcreaterole,
  rolcanlogin
FROM pg_roles
WHERE rolname IN (
                  'idempotent_orders_migrator',
                  'idempotent_orders_runtime'
  )
ORDER BY rolname;

\echo ''
\echo '=== 2. Database ownership ==='
-- expected: idempotent_orders owned by the bootstrap identity (postgres) —
-- ownership stays above the split
SELECT
  datname AS database_name,
  pg_get_userbyid(datdba) AS database_owner
FROM pg_database
WHERE datname = 'idempotent_orders';

\echo ''
\echo '=== 3. Schema ownership ==='
-- expected: idempotent_orders owned by migrator; public untouched under
-- pg_database_owner
SELECT
  nspname AS schema_name,
  nspowner::regrole AS schema_owner
FROM pg_namespace
WHERE nspname IN ('idempotent_orders', 'public')
ORDER BY nspname;

\echo ''
\echo '=== 4. Schema privileges ==='
-- expected: migrator USAGE+CREATE true (owner); runtime USAGE true, CREATE false
SELECT
  role_name AS grantee,
  privilege_type,
  has_schema_privilege(role_name, 'idempotent_orders', privilege_type) AS has_privilege
FROM (
       VALUES
         ('idempotent_orders_migrator'),
         ('idempotent_orders_runtime')
     ) AS roles(role_name)
       CROSS JOIN (
  VALUES
    ('USAGE'),
    ('CREATE')
) AS privileges(privilege_type)
ORDER BY grantee, privilege_type;

\echo ''
\echo '=== 5. Default privileges for future tables/sequences ==='
-- expected: for owner migrator in schema idempotent_orders — runtime granted
-- SELECT/INSERT/UPDATE/DELETE on tables, USAGE/SELECT on sequences; the
-- model's load-bearing mechanism, invisible to \dn+
SELECT
  defaclrole::regrole AS owner_role,
  defaclnamespace::regnamespace AS schema_name,
  CASE defaclobjtype
    WHEN 'r' THEN 'table'
    WHEN 'S' THEN 'sequence'
    WHEN 'f' THEN 'function'
    WHEN 'T' THEN 'type'
    WHEN 'n' THEN 'schema'
    ELSE defaclobjtype::text
    END AS object_type,
  acl.grantee::regrole AS grantee,
  acl.privilege_type,
  acl.is_grantable
FROM pg_default_acl
       CROSS JOIN LATERAL aclexplode(defaclacl) AS acl
WHERE defaclnamespace = 'idempotent_orders'::regnamespace
ORDER BY
  defaclrole::regrole::text,
  defaclobjtype,
  acl.grantee::regrole::text,
  acl.privilege_type;