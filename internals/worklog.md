# worklog.md

The problem's happened-record: what was actually done, that the outputs alone don't
show — appended as work happens, entries self-contained, never rewritten; coupled to
the map by shared step and phase names. Kind law: `knowledge/knowledge.vision.md`.
The map yields to this record when reality corrects orientation, never the reverse.

---

**0001 — preparation / infrastructure established: Execution Environment verified —
podman on the ground.** The environment decided in `log.md` 0005 (podman local
containers) verified on the working machine (Fedora 42, rootless): `podman --version`
→ 5.8.2; `podman info` clean — rootless, crun runtime, netavark networking, overlay
on btrfs. Compose tooling surveyed: the machine carries **two compose providers** —
the canonical **`podman compose`** front door delegating to an external provider
(found: docker-compose v2.39.4 at `~/.docker/cli-plugins/docker-compose`, announcing
itself with a provider banner — informational, not an error), and the standalone
python **`podman-compose`** 1.5.0. Canonical command settled: **`podman compose`**
(podman's own front door, provider-agnostic); the python tool remains present but
unused.

**0002 — preparation / infrastructure established: compose ground born and
validated; two provider behaviors met on the way.** `compose.yaml` born at repo root
(`name: idempotent-orders`, `services: {}` — Infrastructure Services join at their
evaluation). The empty ground surfaced a provider-behavior split, lived in this
order: **(a)** `podman-compose up -d` accepted the empty file and created a **pod**
(the printed hash — podman-compose's per-project grouping unit; not a container, so
`ps` correctly listed nothing); downed and cleaned up. **(b)** the canonical
`podman compose up -d` **refused** the empty file — docker-compose v2 exits with
"no service selected": strict, nothing to start. Resolution: with zero services the
correct verification is **validation, not startup** — `podman compose config`
parsed and echoed the normalized file (`name: idempotent-orders`, `services: {}`)
clean. The file, the front door, and the provider chain are proven; `up`/`ps`/`down`
become meaningful at the first real service. No dummy service added to satisfy the
strict provider — the ground stays constrained to need.

**0003 — preparation / infrastructure established: PostgreSQL up under its
constraints — the role model's first lived application.** The service evaluated in
`log.md` 0006 made real. Ground files landed: the `postgres` service in
`compose.yaml` (postgres:17, named volume, healthcheck, host port as
machine-variance `${POSTGRES_PORT:-5432}`), the bootstrap SQL at
`infrastructure/postgres/init/bootstrap.sql` (the role model instantiated:
`idempotent_orders_migrator` owning schema `idempotent_orders`,
`idempotent_orders_runtime` DML-only via default privileges, search_path set for
both; database owned by the bootstrap identity), the Flyway one-shot behind the
`migrate` profile with `infrastructure/flyway/conf/flyway.conf` (no credentials —
env-passed) and an empty `migrations/` dir, plus `.env.example` → `.env`
(git-ignored; secrets and machine variance only — decided identities stay literal
in their files). Naming note: the volume stays the bare `postgres-data` — compose
prefixes the project name onto volumes and networks by itself, so no manual prefix.
`podman compose config` rendered the whole clean; then `up -d` clean (network +
container created), `ps` → Up, healthy, 5432 published.

**0004 — preparation / infrastructure established: the authority constraint
verified live; Flyway wired as migrator.** The model's checks run against the
running service: **(a)** `\dn+` as the bootstrap identity — schema
`idempotent_orders` owned by `idempotent_orders_migrator`, runtime holding USAGE,
`public` untouched under `pg_database_owner`. **(b)** the constraint's live proof:
`CREATE TABLE` as `idempotent_orders_runtime` → **`ERROR: permission denied for
schema idempotent_orders`** — the running application's identity cannot control
database structure; the principle enforced by the grant system, not convention.
**(c)** `podman compose run --rm flyway info` — image pulled (Flyway OSS 11.20.3),
connected as migrator to `jdbc:postgresql://postgres:5432/idempotent_orders`
(PostgreSQL 17.10), schema empty, history table not yet created, no migrations on
disk — correct: no slice has earned schema yet; the DDL path stands wired and
waiting.

**0005 — preparation / infrastructure established: prior-work bootstrap scripts
arrived as input; ground hardened and re-verified.** The human's prior-project
PostgreSQL scripts (role/database/schema bootstrap plus a catalog verification
suite, built on a three-role variant) arrived as ordinary input and were evaluated
against the standing ground. Adopted: **explicit database connect control**
(`REVOKE CONNECT … FROM PUBLIC` + per-identity grants — the outermost door closed
by grant like every inner one), the **public-schema revoke** (`REVOKE ALL ON SCHEMA
public FROM PUBLIC` — unused made enforced), the **header discipline** (Purpose /
Run as / Database context / Boundary) across the SQL files, and the **verification
suite** — landed as `infrastructure/postgres/verify-database-model.sql`, trimmed to
this project's two-role instantiation: roles and capabilities, database and schema
ownership, the schema-privilege matrix, and the `pg_default_acl` explosion that
makes the model's load-bearing default privileges visible (which `\dn+` cannot
show). Declined: the scripts' idempotent re-run machinery (`IF NOT EXISTS` /
`\gexec` — dead weight under run-once `initdb.d`; the reset path is the re-run)
and the dedicated-admin tier (already placed by the model as the shared-cluster
extension, not the local base). The bootstrap SQL changed → full reset run
(`down --volumes`, `up -d`), the catalog suite and the behavioral DDL-refusal
check re-run clean against the hardened ground.