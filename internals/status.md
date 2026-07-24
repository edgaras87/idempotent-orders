# status.md

*The problem's snapshot. Rewritten freely (at minimum at session end); owes nothing to
its previous version. Subject: the problem only — the node's own standing lives in
`knowledge/knowledge.status.md`.*

**Where things stand:** **`preparation` is exited** — the whole phase: the reasoning
chain (intent, definition, registry — all standing since *problem framed*), the
governed ground (*infrastructure established*), and now **`system bootstrapped`**,
executed under a seven-step change plan. The system is live on the ground: **Java 21 /
Spring Boot 4.1.0 on Maven, Spring JDBC (`JdbcClient`), no ORM** (`log.md` 0007);
the app connects to PostgreSQL as **`idempotent_orders_runtime` — its one identity,
no migration machinery anywhere in the artifact** (the layered claim: artifact shape
first line, grant system last line); Flyway stays compose-side, the migrations' home
confirmed at `infrastructure/flyway/migrations/` and empty until a slice earns
schema. The **evidence harness runs**: Testcontainers on podman's user socket
(config via `~/.testcontainers.properties`), a singleton postgres:17 per test JVM,
**harness-side Flyway** applying the one migrations home before any context boots —
same principle in every tier: something outside the app migrates; the app runs.
Proven end to end: the migration pipeline (history table on the empty set) and the
exit's adversity test — **100 latch-released concurrent requests through
HTTP → app → real PostgreSQL, all green** (`ConcurrentProbeIT`; the probe endpoint
is marked scaffolding, dying at the first real slice). The worklog carries the
step's happened-record, entries 0006–0009, including the lived detours (the
Testcontainers config route, Boot 4's `restclient` modularization). The README's
owed projection delta landed: stack, run/test instructions, the manual linked,
status at the exit.

Position, in the map's names: **`preparation` exited → before `execution`** — S1
(duplicate suppression, registry chosen-next) starts with nothing missing around
it: schema has a DDL path, evidence has a harness, the API has a stack. Entering
execution triggers two owed acts: the **fork's flow-back**
(`knowledge/knowledge.log.md` 0008 — the batch through the human) and the
**first-slice judgment point** (`knowledge/knowledge.log.md` 0006 — now also
carrying the coder-role/IDE-vehicle question, parked there with this step's lived
evidence of the browser loop on code work).

**Shape** *(snapshot; the repo is the master)*:

```text
repo root
├── README.md                     surface current at preparation's exit
├── compose.yaml                  the Execution Environment's declaration
├── .env.example / .env           credentials template / local values (ignored)
├── pom.xml                       Java 21, Boot 4.1.0; runtime deps thin, harness test-scope
├── mvnw / .mvn/                  Maven wrapper
├── src/main/java/com/edge/idempotentorders/
│   ├── IdempotentOrdersApplication.java
│   └── probe/ProbeController.java        scaffolding — dies at the first slice
├── src/main/resources/application.yaml   runtime identity only; health detail on
├── src/test/java/com/edge/idempotentorders/
│   ├── testsupport/              AbstractPostgresIT / AbstractDbIT / AbstractWebDbIT
│   ├── db/MigrationPipelineIT.java
│   ├── probe/ConcurrentProbeIT.java      the exit's adversity proof
│   └── IdempotentOrdersApplicationTests.java
├── infrastructure/
│   ├── postgres/                 init/bootstrap.sql + verify-database-model.sql
│   └── flyway/                   conf + migrations/ (empty until earned)
└── internals/
    ├── project.intent.md         define
    ├── system.definition.md      define
    ├── slices.registry.md        S1 chosen-next
    ├── infrastructure.manual.md  capture — now incl. the Testcontainers test runtime
    ├── infrastructure-coder.manual.md   capture
    ├── log.md                    entries 0001–0007
    ├── status.md
    ├── worklog.md                entries 0001–0009
    └── knowledge/                the working node (its own status inside)
```

**Unsettled:**
- **S1's `specify-correctness`** — execution's first act: the request-identity
  contract (the folded concern 4) opened inside it, per the registry's notes.
- **The flow-back** — due now, at the phase turn (the batch enumerated in the
  node's records).
- **The first-slice judgment point** — thinker-role check plus the parked
  coder/IDE-vehicle question, decided at S1's start on lived evidence.
- **The manuals' genre flips** — both capture; the ground now carries the system
  and the harness, so the flip question ripens as S1 exercises it.
- **The probe's death** — `ProbeController` and its hammer removed when S1's real
  endpoint and evidence land.

**Next:** execution — S1, duplicate suppression, starting at `specify-correctness`.