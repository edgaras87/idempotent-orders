# workbench-postgres-setup.walkthrough.md

> **A workbench walkthrough: PostgreSQL from nothing to a governed, verified
> ground** — the setup sequence, scripts included, ready to walk. A walkthrough is
> an end-to-end *do this* for one concrete capability — narrower than a guide (one
> service, not a step's whole territory), reusable unlike a manual (no single
> project's operating truth). **Not a master of any claim**: the authority shape —
> roles, grants, the why — is mastered in `workbench-postgres-role.model.md`; on
> any conflict, **the model wins** and this doc is corrected to match. The step
> frame around it: `workbench-infrastructure-establishment.guide.md`, moves 3–6.
> **Lived once and not yet workbench-mastered**: distilled from its first walking
> project (worklog 0001–0005 the happened-record), riding that project's flow-back;
> owed confirmation by the second using project — especially where its ground
> differs. **Context it assumes** (noted where it binds): the Execution Environment
> is **podman local containers** driven by a compose file, and **Flyway** runs as a
> compose one-shot as the only DDL path.
>
> Placeholders throughout: `<project>` — underscored in SQL identifiers
> (`<project>_migrator`), hyphenated where your project naming allows it
> (compose project name). Worked example: `idempotent-orders` /
> `idempotent_orders`.

---

## 0 · Preconditions

- podman installed, a compose provider present; `podman compose config` runs clean
  against your repo's `compose.yaml` (canonical command: `podman compose` — the
  front door, whatever provider stands behind it).
- The decisions already made and logged: PostgreSQL is a required Infrastructure
  Service; its constraints are named (migrations-only DDL; role-split authority).
  This walkthrough *implements* those decisions; it does not make them.

## 1 · Declare the service (compose)

Add to `compose.yaml` — a `postgres` service and the Flyway one-shot:

```yaml
services:
  postgres:
    image: docker.io/library/postgres:17
    container_name: <project>-postgres
    environment:
      POSTGRES_PASSWORD: ${POSTGRES_BOOTSTRAP_PASSWORD}
      POSTGRES_DB: <project_db>            # e.g. idempotent_orders
    ports:
      - "${POSTGRES_PORT:-5432}:5432"      # host side = machine variance
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./infrastructure/postgres/init:/docker-entrypoint-initdb.d:ro,Z
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d <project_db>"]
      interval: 5s
      timeout: 3s
      retries: 10

  flyway:                                   # the only DDL path — a one-shot,
    image: docker.io/flyway/flyway:11       # hidden from plain `up`
    profiles: ["migrate"]
    environment:
      FLYWAY_USER: <project>_migrator
      FLYWAY_PASSWORD: ${FLYWAY_MIGRATOR_PASSWORD}
    depends_on:
      postgres:
        condition: service_healthy
    volumes:
      - ./infrastructure/flyway/conf:/flyway/conf:ro,Z
      - ./infrastructure/flyway/migrations:/flyway/sql:ro,Z

volumes:
  postgres-data:        # bare name — compose prefixes the project itself
```

Podman notes: the `:Z` mount flag matters on SELinux hosts; the volume and network
names get the compose project prefix automatically — never write it manually.

## 2 · Split out credentials (`.env`)

`.env` (git-ignored) + committed `.env.example`. Keys: the bootstrap password, the
migrator password (**must equal** the literal in the bootstrap SQL below), optional
`POSTGRES_PORT`. Rule: secrets and machine variance are variables; **decided
identities (db, schema, role names) stay literal** in the files that use them —
changing one is a committed decision, not configuration.

## 3 · Write the bootstrap SQL (the model instantiated)

`infrastructure/postgres/init/bootstrap.sql` — runs **once**, automatically, at the
container's first start against an empty volume, as the bootstrap identity
(`postgres`), **connected to `POSTGRES_DB`**. In model order, comments carried in
full — they are the recall layer:

```sql
-- infrastructure/postgres/init/bootstrap.sql
--
-- Purpose:
--   Instantiate the postgres role model for this project: the two working
--   identities, the application schema, and the privilege boundaries.
--   (model: workbench-postgres-role.model.md; constraints: the project's log)
--
-- Run as:
--   the bootstrap identity (postgres) — runs automatically, ONCE, at the
--   container's first start against an empty volume (docker-entrypoint-initdb.d;
--   files there run in lexical order — irrelevant while this is the only one).
--   Re-run = full reset: podman compose down --volumes, then up.
--
-- Database context:
--   <project_db> — the container's entrypoint creates POSTGRES_DB
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
--   application objects; those arrive solely through the migration tool.

-- the two identities of the authority split
CREATE ROLE <project>_migrator LOGIN PASSWORD '...';
CREATE ROLE <project>_runtime  LOGIN PASSWORD '...';

-- PUBLIC (the grant target: every role, present and future) loses the default
-- right to connect; access to this database exists only by the named grants
-- below. The bootstrap identity needs no grant — superusers bypass checks.
REVOKE CONNECT ON DATABASE <project_db> FROM PUBLIC;
GRANT  CONNECT ON DATABASE <project_db> TO <project>_migrator;
GRANT  CONNECT ON DATABASE <project_db> TO <project>_runtime;

-- the application schema: owned by migrator, consumable by runtime
CREATE SCHEMA <project_schema> AUTHORIZATION <project>_migrator;
GRANT USAGE ON SCHEMA <project_schema> TO <project>_runtime;

-- the DEFAULT SCHEMA named "public" (unrelated to the PUBLIC grant target
-- above) stops being usable by anyone — no side door beside the governed
-- schema. (PG15+ already revokes CREATE; this makes the whole stance explicit.)
REVOKE ALL ON SCHEMA public FROM PUBLIC;

-- the load-bearing lines: every table and sequence a future migration creates
-- arrives already granted to runtime — the split needs no per-migration
-- discipline, so it cannot erode
ALTER DEFAULT PRIVILEGES FOR ROLE <project>_migrator IN SCHEMA <project_schema>
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO <project>_runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE <project>_migrator IN SCHEMA <project_schema>
  GRANT USAGE, SELECT ON SEQUENCES TO <project>_runtime;

-- search_path: where unqualified names (CREATE TABLE orders ..., SELECT * FROM
-- orders) resolve — pinned so migrations and application statements land in the
-- governed schema, never in public. A default for convenience; the authority
-- boundary is the grants above, not this.
ALTER ROLE <project>_migrator SET search_path = <project_schema>;
ALTER ROLE <project>_runtime  SET search_path = <project_schema>;
```

Who creates the database: **the container**, from `POSTGRES_DB` — owned by the
bootstrap identity; migrator owns only the schema. No `CREATE DATABASE` in the
script.

## 4 · Land the verification suite

`infrastructure/postgres/verify-database-model.sql` — beside `init/`, **not in it**
(run on demand, never at container start). It queries the catalog against the
model's claims; expected results ride as comments so the reader needs no other doc
open:

```sql
-- Purpose:
--   Verify the database role, ownership, schema, and privilege model against
--   the postgres role model's claims — catalog-level, on demand.
--
-- Run as:
--   the bootstrap identity (postgres), on demand:
--   podman exec -i <project>-postgres \
--     psql -U postgres -d <project_db> < infrastructure/postgres/verify-database-model.sql
--
-- Database context:
--   <project_db>
--
-- Boundary:
--   Verifies the bootstrap model only — no application tables are required to
--   exist; runtime privileges on real tables are verified after real
--   migrations create them. The behavioral check (runtime DDL refusal) lives
--   beside this script (step 7), not in it.

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
    '<project>_migrator',
    '<project>_runtime'
)
ORDER BY rolname;

\echo ''
\echo '=== 2. Database ownership ==='
-- expected: <project_db> owned by the bootstrap identity (postgres) —
-- ownership stays above the split
SELECT
    datname AS database_name,
    pg_get_userbyid(datdba) AS database_owner
FROM pg_database
WHERE datname = '<project_db>';

\echo ''
\echo '=== 3. Schema ownership ==='
-- expected: <project_schema> owned by migrator; public untouched under
-- pg_database_owner
SELECT
    nspname AS schema_name,
    nspowner::regrole AS schema_owner
FROM pg_namespace
WHERE nspname IN ('<project_schema>', 'public')
ORDER BY nspname;

\echo ''
\echo '=== 4. Schema privileges ==='
-- expected: migrator USAGE+CREATE true (owner); runtime USAGE true, CREATE false
SELECT
    role_name AS grantee,
    privilege_type,
    has_schema_privilege(role_name, '<project_schema>', privilege_type) AS has_privilege
FROM (
    VALUES
        ('<project>_migrator'),
        ('<project>_runtime')
) AS roles(role_name)
CROSS JOIN (
    VALUES
        ('USAGE'),
        ('CREATE')
) AS privileges(privilege_type)
ORDER BY grantee, privilege_type;

\echo ''
\echo '=== 5. Default privileges for future tables/sequences ==='
-- expected: for owner migrator in <project_schema> — runtime granted
-- SELECT/INSERT/UPDATE/DELETE on tables, USAGE/SELECT on sequences,
-- is_grantable false throughout (authority does not cascade); the model's
-- load-bearing mechanism, invisible to \dn+
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
WHERE defaclnamespace = '<project_schema>'::regnamespace
ORDER BY
    defaclrole::regrole::text,
    defaclobjtype,
    acl.grantee::regrole::text,
    acl.privilege_type;
```

## 5 · Wire Flyway (config + empty migrations)

`infrastructure/flyway/conf/flyway.conf` — no credentials (env-passed by compose);
the url speaks the compose network's service name:

```
flyway.url=jdbc:postgresql://postgres:5432/<project_db>
flyway.schemas=<project_schema>
flyway.locations=filesystem:/flyway/sql
```

`infrastructure/flyway/migrations/` — empty (+`.gitkeep`) until work earns schema;
naming, when it does: `V<n>__<description>.sql`. **No grant statements in
migrations, ever** — default privileges handle it.

## 6 · Bring it up

```sh
podman compose config      # sanity: interpolation resolves, file renders
podman compose up -d       # first start: db created, bootstrap SQL runs
podman compose ps          # expect: <project>-postgres Up (healthy)
```

## 7 · Verify — both ways, always

**Catalog check** (the ground's state vs. the model's claims — roles and
capabilities, database and schema ownership, the schema-privilege matrix, and the
`pg_default_acl` explosion that makes default privileges visible; `\dn+` cannot
show them):

```sh
podman exec -i <project>-postgres \
  psql -U postgres -d <project_db> < infrastructure/postgres/verify-database-model.sql
```

**Behavioral check** (the constraint attempted and refused, live):

```sh
podman exec -it <project>-postgres \
  psql -U <project>_runtime -d <project_db> -c 'CREATE TABLE t(i int);'
# expected: ERROR: permission denied for schema <project_schema>
```

**Flyway as migrator** (connects, sees the schema, empty history — correct on a
fresh ground):

```sh
podman compose run --rm flyway info
```

## 8 · Know the reset path

The bootstrap SQL runs only against an empty volume. After any change to it:

```sh
podman compose down --volumes   # drops all data
podman compose up -d            # re-runs the bootstrap
```

…then re-run step 7, both checks.

## 9 · Hand off

The connection facts the system's code needs: host `localhost:${POSTGRES_PORT:-5432}`
from outside, `postgres:5432` container-to-container; database `<project_db>`,
schema `<project_schema>`; **the application connects as `<project>_runtime` and
nothing else** — migrator is the migration tool's identity alone; the bootstrap
identity is an occasional admin lens, never wired in. Day-to-day IDE/psql
inspection: runtime.

---

## Lifecycle note

Born from its first walk (the walking project's worklog is the happened-record) at
that project's request: the reuse question answered by a walkthrough rather than
deferred. Reaches workbench mastery through the project's flow-back; owed
confirmation, correction, or generalization by the second using project. The model
masters the authority shape — corrections flow model → walkthrough, never the
reverse.