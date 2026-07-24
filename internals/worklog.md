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

**0006 — preparation / system bootstrapped: Spring Boot skeleton born, pruned,
verified starting on JDK 21.** The stack decided in `log.md` 0007 made real at its
first step. Generated via Spring Initializr (Maven, Java 21, jar; group `com.edge`,
artifact `idempotent-orders`, package `com.edge.idempotentorders`) with exactly two
dependencies — Spring Web and Actuator — persistence, migration tooling,
Testcontainers, validation, and convenience tooling all deliberately unselected,
each arriving only at the step or slice that earns it. Extracted into the repo root
beside `internals/` (layout settled: node beside code, standard Maven layout, one
base package). Pruned on arrival: `HELP.md`, empty `static/` and `templates/`,
generated `application.properties` replaced by `application.yaml` carrying the
application name only; generated empty pom metadata blocks removed; the repo's
`.gitignore` extended with the Maven/build set rather than replaced by the
generated one; generated `.gitattributes` kept. Verified: `./mvnw test` green
(generated context test, no database involved); `./mvnw spring-boot:run` starts
announcing Java 21; `GET /actuator/health` → `{"status":"UP"}`; graceful Ctrl+C
shutdown. The skeleton runs database-blind by design — the ground wiring is the
next step's own change.

**0007 — preparation / system bootstrapped: skeleton wired to the ground as the
runtime identity; the wiring made visible through health.** The app met the
established infrastructure per the coder manual: `spring-boot-starter-jdbc` and the
PostgreSQL driver (runtime scope) added — no `-test` companions exist for either on
the Boot 4 line; database access is verified through the Testcontainers harness
(next step) instead. Datasource configured to
`localhost:${POSTGRES_PORT:5432}/idempotent_orders` as
**`idempotent_orders_runtime`** — the application's one identity; password
env-passed with the local literal `runtime_local` as default; **no `currentSchema`
parameter** — `search_path` is pinned server-side for the identity, so unqualified
names resolve without client-side help. **No migrator anything, anywhere in the
configuration** — the layered claim's first line now real in the artifact. Actuator
health opened to component detail (`show-details: always` — local project), making
the database connection a visible part of `/actuator/health` with zero custom code.
Verified: ground up (`podman compose up -d`), `./mvnw spring-boot:run`, health →
`{"status":"UP"}` with the `db` component UP against PostgreSQL; `./mvnw test`
green — the context test needs no database (the pool connects lazily; nothing in
the skeleton touches the datasource yet), so the app remains startable with the
ground down, degrading only in what health reports.

**0008 — preparation / system bootstrapped: evidence harness stood up —
Testcontainers on podman, harness-side Flyway.** Two things stood up together.
**The test runtime:** Testcontainers speaks the Docker API; on this ground that is
podman's rootless user socket — `podman.socket` enabled, the socket path and
`ryuk.disabled=true` (the cleanup sidecar misbehaves under rootless podman) set via
`~/.testcontainers.properties`, chosen over env vars so IDE test runs need no
inherited shell exports; setup, verification, and troubleshooting landed as the
operator manual's new *Test runtime* section (a light infrastructure re-entry —
material adapted from the human's prior-project walkthrough, arriving as ordinary
input). **The harness:** test-scope dependencies added — `testcontainers-postgresql`
(Testcontainers 2 artifact names on the Boot 4 line), `spring-boot-testcontainers`
(`@ServiceConnection` wiring), and Flyway (`flyway-core` +
`flyway-database-postgresql`) as a **harness tool, never a runtime dependency**;
Surefire's include set widened so `*IT.java` runs under the one standard
`./mvnw test`. Three base classes under `testsupport/`: `AbstractPostgresIT` — one
singleton postgres:17 container per test JVM (same major as the ground), started in
the static initializer with lifecycle deliberately ours, `@ServiceConnection`
wiring the test datasource from the field, and the harness-side migrate applying
`filesystem:infrastructure/flyway/migrations` — the one migrations home, no
classpath copy to drift — before any context boots (the principle held in the test
tier: something outside the app migrates; the app runs); `AbstractDbIT` — context
without HTTP, `JdbcClient` injected to match the app's own data-access choice;
`AbstractWebDbIT` — random-port full stack, Boot 4's `resttestclient`
TestRestTemplate packages. No test profile born — `@ServiceConnection` overrides
the datasource, so the single `application.yaml` stands. Proof test:
`MigrationPipelineIT` — `flyway_schema_history` exists and holds zero applied
migrations, exactly right for the deliberately empty migrations home; the query
succeeding is the pipeline demonstrated end to end (container → migrate → context →
query). Verified: `./mvnw test` green — the image pulled, the harness loop live.

**0009 — preparation / system bootstrapped: first adversity test green — the
harness proven to create concurrency through the full stack.** The map exit's
"trivial adversity-generating test" made real: a throwaway probe endpoint
(`GET /probe/db` — `SELECT 1` via `JdbcClient` as the runtime identity, javadoc'd
as scaffolding that dies when the first real slice lands) hammered by
`ConcurrentProbeIT` on the `AbstractWebDbIT` base — **100 requests lined up behind
a latch and released at one instant** (virtual threads, Java 21), each a genuine
HTTP → app → real-PostgreSQL round-trip; every response 200 with the correct
body. Deliberately trivial — the probe holds no invariant to violate; what the
green run proves is the machinery: the harness *creates* the definition's dominant
adversity dimension (concurrency) through the same door a real caller uses,
against the same database major the ground runs. The pool queues under the load
(Hikari's default sizing) and all requests still complete — noise-free at this
scale. S2's race evidence is this exact shape aimed at a real invariant; the
chosen slice has somewhere to land. One Boot-4 modularization hiccup lived on
the way: the web test context failed to load —
`NoClassDefFoundError: RestTemplateBuilder` — because `TestRestTemplate`'s
`resttestclient` module introspects `RestTemplateBuilder` from the separate
`spring-boot-restclient` module, which nothing pulled in; fixed by adding that
module test-scope (the application itself makes no outbound HTTP calls).
Verified: `./mvnw test` green — context test, migration pipeline, and the
concurrent probe on one shared container.