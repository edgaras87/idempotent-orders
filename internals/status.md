# status.md

*The problem's snapshot. Rewritten freely (at minimum at session end); owes nothing to
its previous version. Subject: the problem only — the node's own standing lives in
`knowledge/knowledge.status.md`.*

**Where things stand:** **`execution` is open — S1 at `specify-correctness`.** The
two acts owed at the phase turn are done on the node's side: the fork's flow-back
handed and accepted, the branches refreshed; the first-slice judgment point decided
(`knowledge/knowledge.log.md` 0014) — steward-only confirmed for the problem's
reasoning, the **coder role born**, and the **slice flow set as a role-split
shape**: the steward specifies the slice's correctness in the browser and hands a
spec; the coder plans and implements from it in the IDE; the steward documents the
evidence back to this plane; the human reviews and commits everything.

The ground S1 lands on is unchanged since preparation's exit: Java 21 / Spring
Boot 4.1.0 on Maven, Spring JDBC (`JdbcClient`), no ORM; the app one identity
(`idempotent_orders_runtime`, DML only); Flyway compose-side, the migrations home
at `infrastructure/flyway/migrations/` still empty — **S1 earns the first
schema**; the evidence harness proven (Testcontainers on podman, harness-side
Flyway, the 100-request concurrent probe green). The probe (`ProbeController` and
its hammer) still stands, dying when S1's real endpoint and evidence land.

**S1 — duplicate suppression** (registry chosen-next): first act is the steward's
`specify-correctness` — the slice's invariants, guarantees, and evidence
expectations as the handoff spec, with the **request-identity contract** (the
folded concern 4) opened inside it per the registry's notes. The spec artifact's
kind and name are birthed there — this plane's decisions, recorded in `log.md`.

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
│   └── probe/ProbeController.java        scaffolding — dies at S1's real endpoint
├── src/main/resources/application.yaml   runtime identity only; health detail on
├── src/test/java/com/edge/idempotentorders/
│   ├── testsupport/              AbstractPostgresIT / AbstractDbIT / AbstractWebDbIT
│   ├── db/MigrationPipelineIT.java
│   ├── probe/ConcurrentProbeIT.java      preparation's adversity proof
│   └── IdempotentOrdersApplicationTests.java
├── infrastructure/
│   ├── postgres/                 init/bootstrap.sql + verify-database-model.sql
│   └── flyway/                   conf + migrations/ (empty — S1 earns the first)
└── internals/
    ├── project.intent.md         define
    ├── system.definition.md      define
    ├── slices.registry.md        S1 chosen-next
    ├── infrastructure.manual.md  capture
    ├── infrastructure-coder.manual.md   capture — the coder's ground handoff
    ├── log.md                    entries 0001–0007
    ├── status.md
    ├── worklog.md                entries 0001–0009
    └── knowledge/                the working node (its own status inside)
```

**Unsettled:**
- **S1's `specify-correctness`** — the next act: the spec (kind and name born
  there), the request-identity contract inside it.
- **The spec handoff and the coder's first pass** — the role-split shape's first
  lived exercise; its strain evidence read at S1's exit.
- **The manuals' genre flips** — both capture; ripening as S1 exercises the
  ground.
- **The probe's death** — removed when S1's real endpoint and evidence land.
- **The registry after S1** — S1's closing on evidence picks the next chosen-next
  (concurrent-duplicate race the expected candidate; the registry decides).

**Next:** S1's `specify-correctness`, steward, next session.