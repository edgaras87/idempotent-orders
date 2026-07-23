# infrastructure-coder.manual.md

> **Genre: capture** — written at the ground's completion; flips to define by logged
> decision as the ground proves stable under use. **Audience: the coder** — the role
> (human or AI) building the system on this ground: what exists, how to connect, what
> is allowed. Setup and manual operation are the operator's manual
> (`infrastructure.manual.md`); the authority reasoning is the role model
> (`knowledge/realization/workbench-postgres-role.model.md`). This doc masters the
> infrastructure vocabulary (adopted: `log.md` 0005) and this project's concrete
> wiring — it restates neither of its references.

## Vocabulary (mastered here)

- **Execution Environment** — the local runtime host for infrastructure.
- **Infrastructure Service** — a capability provided to the system.
- **Infrastructure Service Constraint** — a requirement or limitation imposed on an
  infrastructure service.

## Execution Environment — podman local containers

Declared in the repo root's `compose.yaml` (project `idempotent-orders`); operated
with **`podman compose`** (the canonical command). The ground must be up for
anything below to be reachable: `podman compose up -d`, status via
`podman compose ps`.

## Infrastructure Service — PostgreSQL

The project's only Infrastructure Service (`log.md` 0006). PostgreSQL 17.

**Where it is:**

| From | Address |
|---|---|
| the host (application run locally, tests, IDE) | `localhost:5432` — or `POSTGRES_PORT` from `.env` if overridden |
| another container on the compose network | `postgres:5432` — the service name |

**What it holds:** database **`idempotent_orders`** (connect to it, not to the
`postgres` maintenance database), schema **`idempotent_orders`** — the only
application surface; `public` is stripped and unusable. Both working identities have
`search_path` pinned to the schema: unqualified names (`orders`, not
`idempotent_orders.orders`) resolve correctly in migrations and application SQL.

**Who you are — the identities:**

- **`idempotent_orders_runtime`** — **the application's identity; the only one
  application code ever uses.** DML only: SELECT/INSERT/UPDATE/DELETE on tables,
  USAGE/SELECT on sequences — granted automatically on everything migrations
  create. Any DDL attempt is refused by the database.
- **`idempotent_orders_migrator`** — **Flyway's identity, nothing else's.** Never in
  application configuration, never in a tool's saved connection.
- **`postgres`** (the bootstrap identity) — admin inspection only, deliberate, never
  wired into anything.

Local-dev credentials: the bootstrap password in `.env`; the working identities'
passwords literal in `infrastructure/postgres/init/bootstrap.sql` (runtime:
`runtime_local`).

**The constraints** (Infrastructure Service Constraints, `log.md` 0006 — one
principle: the running application must not control database structure):

1. **Flyway is the only DDL path.** All schema — tables, indexes, constraints —
   arrives as versioned migrations. No ORM auto-DDL, no `CREATE` in application
   code, no manual DDL: the grants refuse it anyway.
2. **Roles split authority** — enforced by the database, not convention. Build
   accordingly: the application assumes structure exists; migrations make it exist.

## Migrations — how schema changes are made

Migrations live in `infrastructure/flyway/migrations/` (provisional home — may move
beside the system's code at *system bootstrapped*; empty until a slice earns
schema). Flyway naming: `V<n>__<description>.sql` (e.g. `V1__create_orders.sql`).

```sh
podman compose run --rm flyway migrate    # apply pending
podman compose run --rm flyway info       # history + pending status
podman compose run --rm flyway validate   # applied vs. on-disk consistency
```

Flyway connects as migrator (credentials env-passed from `.env`); migrations run
with `search_path` already on the governed schema. Everything a migration creates
is instantly readable/writable by runtime — no grant statements in migrations,
ever (default privileges handle it; per-migration grants would erode the model).

## Connecting from tools (IDE, psql)

- **Day-to-day data source:** `localhost:5432`, database `idempotent_orders`, user
  `idempotent_orders_runtime` — you see exactly what the application sees, and the
  tool physically cannot alter structure (schema-changing UI actions fail with
  permission denied: the constraint working, not a bug).
- **Admin lens** (deliberate, occasional): user `postgres` against database
  `idempotent_orders` for content, or against the `postgres` maintenance database
  for cluster-wide views (databases and roles are cluster-wide; schemas and tables
  are per-database — no connection sees another database's content).
- **Never** save a migrator connection in a tool.

## Verification, when in doubt

The operator's manual carries both checks — the catalog suite
(`infrastructure/postgres/verify-database-model.sql`) and the behavioral
DDL-refusal test. Run them after any ground change or reset.