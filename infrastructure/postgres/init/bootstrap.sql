-- infrastructure/postgres/init/bootstrap.sql
--
-- Purpose:
--   Instantiate the postgres role model for this project: the two working
--   identities, the application schema, and the privilege boundaries.
--   (model: internals/knowledge/realization/workbench-postgres-role.model.md;
--    constraints: internals/log.md 0006)
--
-- Run as:
--   the bootstrap identity (postgres) — runs automatically, ONCE, at the
--   container's first start against an empty volume (docker-entrypoint-initdb.d;
--   files there run in lexical order — irrelevant while this is the only one).
--   Re-run = full reset: podman compose down --volumes, then up.
--
-- Database context:
--   idempotent_orders — the container's entrypoint creates POSTGRES_DB
--   (compose.yaml) at first start, then runs this script CONNECTED TO IT:
--   schema and grant statements below land in this database. CREATE ROLE is
--   the exception — roles are cluster-wide, in no database.
--
-- Local development note:
--   Role passwords are explicit local-dev placeholders, coupled to .env
--   (FLYWAY_MIGRATOR_PASSWORD must equal the migrator password here).
--
-- Boundary:
--   Identities, schema, and privilege boundaries only — no tables, no
--   application objects; those arrive solely through Flyway migrations.

-- the two identities of the authority split
CREATE ROLE idempotent_orders_migrator LOGIN PASSWORD 'migrator_local';
CREATE ROLE idempotent_orders_runtime  LOGIN PASSWORD 'runtime_local';

-- PUBLIC (the grant target: every role, present and future) loses the default
-- right to connect; access to this database exists only by the named grants
-- below. The bootstrap identity needs no grant — superusers bypass checks.
REVOKE CONNECT ON DATABASE idempotent_orders FROM PUBLIC;
GRANT  CONNECT ON DATABASE idempotent_orders TO idempotent_orders_migrator;
GRANT  CONNECT ON DATABASE idempotent_orders TO idempotent_orders_runtime;

-- the application schema: owned by migrator, consumable by runtime
CREATE SCHEMA idempotent_orders AUTHORIZATION idempotent_orders_migrator;
GRANT USAGE ON SCHEMA idempotent_orders TO idempotent_orders_runtime;

-- the DEFAULT SCHEMA named "public" (unrelated to the PUBLIC grant target
-- above) stops being usable by anyone — no side door beside the governed
-- schema. (PG15+ already revokes CREATE; this makes the whole stance explicit.)
REVOKE ALL ON SCHEMA public FROM PUBLIC;

-- the load-bearing lines: every table and sequence a future migration creates
-- arrives already granted to runtime — the split needs no per-migration
-- discipline, so it cannot erode
ALTER DEFAULT PRIVILEGES FOR ROLE idempotent_orders_migrator IN SCHEMA idempotent_orders
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO idempotent_orders_runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE idempotent_orders_migrator IN SCHEMA idempotent_orders
  GRANT USAGE, SELECT ON SEQUENCES TO idempotent_orders_runtime;

-- search_path: where unqualified names (CREATE TABLE orders ..., SELECT * FROM
-- orders) resolve — pinned so migrations and application statements land in the
-- governed schema, never in public. A default for convenience; the authority
-- boundary is the grants above, not this.
ALTER ROLE idempotent_orders_migrator SET search_path = idempotent_orders;
ALTER ROLE idempotent_orders_runtime  SET search_path = idempotent_orders;