# workbench-spring-boot-bootstrap.walkthrough.md

> **A workbench walkthrough — an end-to-end *do this* for one concrete capability:**
> bootstrapping a Spring Boot system for the *system bootstrapped* step, on an
> already-established PostgreSQL ground. Distilled from one project's lived pass
> (its plan, worklog, and corrections); **not yet workbench-mastered** — the
> `workbench-` prefix is destination lineage; owed confirmation by the second
> bootstrapping project. **Masters nothing:** the stack decision itself belongs to
> the step's own evaluation (fluency and audience are deciding inputs — this
> walkthrough applies only if that evaluation lands on Spring); the ground's
> authority shape is `workbench-postgres-role.model.md`'s; the ground's setup is
> `workbench-postgres-setup.walkthrough.md`'s. On any conflict, those win.
>
> **Assumptions:** the infrastructure step is exited per
> `workbench-infrastructure-establishment.guide.md` — PostgreSQL runs under the
> role split (`<project>_migrator` / `<project>_runtime`), Flyway is a compose-side
> one-shot, migrations live at `infrastructure/flyway/migrations/` (empty).
> Environment: podman (rootless). Stack line: **Spring Boot 4.x, Java 21, Maven** —
> Boot 4's renamed modules are load-bearing throughout; on Boot 3 the names differ.
>
> Placeholders: `<project>` (e.g. `idempotent-orders`), `<project_db>` (underscored:
> `idempotent_orders`), `<group>` (e.g. `com.edge`), `<base-package>` (e.g.
> `com.edge.idempotentorders`).

## The shape

Five commits, each verified before committed; the app ends **wired to the real
ground as the runtime identity, carrying no migration machinery**, with an evidence
harness proven able to create adversity. The governing principle at every tier:
*something outside the app migrates; the app runs.*

```text
1. decide(record):  stack decision in the project log
2. define(app):     bootstrap Spring Boot application skeleton
3. revise(app):     wire the runtime datasource to the established ground
4. define(app):     stand up Testcontainers evidence harness with harness-side Flyway
5. define(app):     prove the evidence harness with a concurrent adversity probe
   (then the step's exit: surface delta + statuses — the adopting project's records)
```

---

## 1. Record the stack decision

Before any code: the project's log gets the decision — the stack, **Flyway staying
outside the app** (the application knows one identity, the runtime role; no
migrator credentials in any profile; the claim layered — artifact shape first line,
grant system last line), and the migrations' home confirmed. Why first: every
commit after this applies the decision; the record is what makes it referenceable
instead of re-argued.

**Trap, named early:** the standard Spring recipe (in-app Flyway with dual
credentials, JPA with `ddl-auto: validate`) quietly reverses the ground's
constraint — *the running application must not control database structure*. Do not
adopt it here. No JPA, no in-app Flyway, no `-data-` starters.

## 2. Bootstrap the application skeleton

**Spring Initializr** (start.spring.io):

```text
Project: Maven · Language: Java · Spring Boot: latest stable (note the version —
later modules must match the line) · Group: <group> · Artifact: <project> ·
Package: <base-package> · Packaging: Jar · Java: 21
Dependencies — exactly two: Spring Web (Boot 4 names it Web MVC), Actuator
```

Deliberately unselected, each arriving only when earned: JPA (never — plain JDBC),
Flyway (never in-app), PostgreSQL Driver + JDBC (commit 3), Testcontainers
(commit 4), Validation / DevTools / Lombok (not earned by a bootstrap).

**Extract into the existing repo root** (beside the node and `infrastructure/`) —
the never-covered case in fresh-project tutorials:

- **Keep:** `pom.xml`, wrapper (`mvnw`, `mvnw.cmd`, `.mvn/`), application class,
  generated context test, `.gitattributes` (merge its wrapper lines — `/mvnw` LF,
  `*.cmd` CRLF — into the repo's existing file).
- **Remove:** `HELP.md`, empty `static/` and `templates/`,
  `application.properties` (replace with `application.yaml` carrying
  `spring.application.name: <project>` only), any accidental Testcontainers stubs.
- **Merge, don't overwrite:** the repo's `.gitignore` (add `target/`, IDE, OS, and
  `*.log` sets) and `README.md`.
- **pom cleanup:** delete the generated empty metadata blocks (`<url/>`,
  `<licenses>`, `<developers>`, `<scm>`); fill `<description>` from the intent;
  confirm `<java.version>21</java.version>`. Boot 4 pairs runtime starters with
  `-test` companions (`spring-boot-starter-webmvc` + `-webmvc-test`,
  `-actuator` + `-actuator-test`) — state the pairing convention once as a comment.

**Verify:** `./mvnw test` green (context test, no DB); `./mvnw spring-boot:run`
announces **Java 21** (align IDE/terminal JDK if not); `/actuator/health` → UP.
**Gives us:** a running, committable system; layout settled — node beside code.

## 3. Wire the runtime datasource

**pom:** `spring-boot-starter-jdbc` + `org.postgresql:postgresql` (runtime scope).
No `-test` companions exist for these on the Boot 4 line — real database access is
verified by the harness, next commit.

**application.yaml:**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:${POSTGRES_PORT:5432}/<project_db>
    username: <project>_runtime
    password: ${DB_RUNTIME_PASSWORD:<local literal from the ground's bootstrap SQL>}
management:
  endpoint:
    health:
      show-details: always   # local project: the db component visible in health
```

Three deliberate absences, each a decision made visible: **no `currentSchema`**
(the ground pins `search_path` server-side per the role model); **no migrator
anything, in any profile** (the layered claim's first line); **no test profile**
(the harness overrides the datasource — commit 4). Comment the yaml with the why —
it is where a reviewer looks first.

**Verify:** ground up (`podman compose up -d`), run, `/actuator/health` → UP with
the `db` component UP. Note: `./mvnw test` stays green with the ground *down* —
the pool connects lazily; the app degrades only in what health reports.
**Gives us:** the empty-but-running skeleton wired to the real service, the
claim-shape inspectable in the artifact.

## 4. Stand up the evidence harness

Two grounds stand up together.

**The test runtime — Testcontainers on podman** (once per machine; full detail
belongs in the adopting project's operator manual):

```sh
systemctl --user enable --now podman.socket
```

then `~/.testcontainers.properties` — **in `$HOME`, not the project root** (in the
project root it is read by nobody; lived as a long "no valid Docker environment"
hunt):

```properties
docker.host=unix:///run/user/<uid>/podman/podman.sock
ryuk.disabled=true
```

Ryuk (the cleanup sidecar) misbehaves under rootless podman — disabling it is the
accepted trade; a hard-killed test JVM can strand a throwaway container
(`podman ps` / `podman rm -f`). **Second lived trap:** systemd can report the
socket *active (listening)* while the socket **file is missing** — active is not
enough; recover with `systemctl --user stop podman.socket podman.service &&
systemctl --user start podman.socket`, then `ls -l` the file.

**pom — test scope only** (Testcontainers 2.x artifact names on the Boot 4 line):

```xml
org.testcontainers:testcontainers-postgresql
org.springframework.boot:spring-boot-testcontainers    <!-- @ServiceConnection -->
org.springframework.boot:spring-boot-restclient        <!-- see the trap below -->
org.flywaydb:flyway-core                               <!-- HARNESS tool, never runtime -->
org.flywaydb:flyway-database-postgresql
```

**Boot 4 trap, lived:** `TestRestTemplate` lives in the `resttestclient` module
whose auto-configuration introspects `RestTemplateBuilder` from the *separate*
`spring-boot-restclient` module — without it the web test context dies with
`NoClassDefFoundError: RestTemplateBuilder`. Add it test-scope.

**Surefire:** widen includes to `**/*IT.java` (plus the `*Test/*Tests` defaults)
so integration tests run under the one standard `./mvnw test`.

**Three base classes** under `<base-package>.testsupport`:

- `AbstractPostgresIT` — the singleton pattern: one `PostgreSQLContainer`
  (**same major as the ground**) per test JVM, started in a static initializer
  (lifecycle deliberately ours — no `@Testcontainers`/`@Container`),
  `@ServiceConnection` on the field wiring the test datasource, and immediately
  after start the **harness-side migrate**:

  ```java
  Flyway.configure()
        .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
        .locations("filesystem:infrastructure/flyway/migrations")   // the ONE home
        .load().migrate();
  ```

  The harness plays the compose one-shot's role against the throwaway database —
  the app under test boots in exactly its shipped posture. `filesystem:`, never a
  classpath copy: one migrations home, nothing to drift.
- `AbstractDbIT` — `@SpringBootTest(webEnvironment = NONE)` extending the above;
  inject the app's own data-access choice (`JdbcClient`).
- `AbstractWebDbIT` — `RANDOM_PORT` + `@AutoConfigureTestRestTemplate`; Boot 4
  imports are `org.springframework.boot.resttestclient.*`.

**Proof test** (`MigrationPipelineIT` on `AbstractDbIT`):
`select count(*) from flyway_schema_history` → **0** — the table existing proves
migrate ran (Flyway births it even on an empty set); zero applied is correct for a
ground where migrations are earned. The first real migration turns the count
positive without changing what the test proves.

**Verify:** `./mvnw test` — image pulls, all green.
**Gives us:** the harness every slice's evidence runs on: real PostgreSQL, the
real migration path, the app in production posture.

## 5. Prove the harness with a concurrent adversity probe

The exit demands one **trivial adversity-generating test** — proving the
*machinery* before any invariant exists, so that from the first slice on, a red
test has exactly one possible cause. Two files:

- **`<base-package>.probe.ProbeController`** (`src/main`) — one endpoint, a real
  round-trip (`select 1` via `JdbcClient` as runtime). Javadoc it **SCAFFOLDING —
  dies when the first real slice lands**; it must never grow business meaning.
- **`ConcurrentProbeIT`** on `AbstractWebDbIT` (`src/test`) — N (e.g. 100)
  requests lined up behind a `CountDownLatch` and released at one instant
  (virtual threads on Java 21), each a full HTTP → app → real-PostgreSQL
  round-trip; assert every response 2xx with the correct body. The latch-release
  pattern *is* the later race evidence's shape, aimed at nothing yet.

**Verify:** `./mvnw test` green. **Gives us:** the exit condition — the harness
demonstrably *creates* concurrency through the same door a real caller uses; the
chosen slice has somewhere to land.

## The step's exit (the adopting project's own records)

Surface delta at the exit anchor (stack, run/test commands, operator manual
linked; honest "no business behavior yet"); statuses rewritten; the probe's death
scheduled at the first slice. Commit vocabulary per the git convention (`app`
scope for the system's own source).

## Lived traps, compressed (the recall list)

1. Boot 4 renames: `spring-boot-starter-web` → `-webmvc`; `-test` companions per
   starter; `TestRestTemplate` → `org.springframework.boot.resttestclient.*`.
2. `spring-boot-restclient` must be added (test scope) or the web test context
   fails on `RestTemplateBuilder`.
3. Testcontainers 2.x artifact names: `testcontainers-postgresql`, package
   `org.testcontainers.postgresql`.
4. `~/.testcontainers.properties` binds only from `$HOME` — never the project root.
5. podman socket can be *active* with the socket file missing — restart the user
   units and `ls` the file.
6. Ryuk off under rootless podman; accept occasional stranded throwaways.
7. No dummy baseline migration — Flyway's history table is born by a
   zero-migration migrate; keep the migrations home honestly empty until earned.