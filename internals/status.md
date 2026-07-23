# status.md

*The problem's snapshot. Rewritten freely (at minimum at session end); owes nothing to
its previous version. Subject: the problem only — the node's own standing lives in
`knowledge/knowledge.status.md`.*

**Where things stand:** **`infrastructure established` is exited** — the governed
ground the system will run on stands whole and verified. The Execution Environment is
podman local containers (`log.md` 0005; canonical command `podman compose`, declared
in the root `compose.yaml`). The Infrastructure Service set is **PostgreSQL alone**
(`log.md` 0006 — the not-provisioned list stated), running under its two constraints,
both realizations of *database availability → database authority control*: **Flyway
is the only DDL path** (a one-shot behind the `migrate` profile, connecting as
migrator; migrations empty until a slice earns schema), and **roles split
authority** — `idempotent_orders_migrator` owns the schema and all structure,
`idempotent_orders_runtime` is DML-only, the split enforced by the grant system and
**demonstrated live** (catalog suite + the DDL refusal; worklog 0004–0005). Both
manuals exist: `infrastructure.manual.md` (operator) and
`infrastructure-coder.manual.md` (coder handoff, vocabulary master). The worklog runs
(entries 0001–0005, the step's happened-record).

Position, in the map's names: **`preparation` → before `system bootstrapped`** — the
last phase-step: the system born on the established infrastructure (stack chosen and
initialized, layout settled, skeleton wired to the real service as runtime, the
evidence harness running one trivial adversity-generating test). Its exit closes
preparation — and triggers the fork's flow-back (`knowledge/knowledge.log.md` 0008)
and the README's owed projection delta (stack + how to run, linking the manual).

**Shape** *(snapshot; the repo is the master)*:

```text
repo root
├── README.md                     surface at problem framed's exit (delta owed at next exit)
├── compose.yaml                  the Execution Environment's declaration
├── .env.example / .env           credentials template / local values (ignored)
├── infrastructure/
│   ├── postgres/
│   │   ├── init/bootstrap.sql    the role model instantiated — runs at first start
│   │   └── verify-database-model.sql   catalog verification suite
│   └── flyway/
│       ├── conf/flyway.conf      migrator connection (credentials env-passed)
│       └── migrations/           empty until a slice earns schema (home provisional)
└── internals/
    ├── project.intent.md         the why — define
    ├── system.definition.md      the what — define
    ├── slices.registry.md        the control point — S1 chosen-next
    ├── infrastructure.manual.md  operator manual — capture
    ├── infrastructure-coder.manual.md   coder manual — capture
    ├── log.md                    problem log, entries 0001–0006
    ├── status.md
    ├── worklog.md                entries 0001–0005
    └── knowledge/                the working node (its own status inside)
```

**Unsettled:**
- **System bootstrapped** — the next phase-step: stack and tooling chosen (the
  step's own evaluation), layout beside the node, the skeleton wired to PostgreSQL
  as runtime, the evidence harness green on one trivial adversity test.
- **Migrations' home** — provisional at `infrastructure/flyway/migrations/`; may
  move beside the system's code when the stack lands (only the compose mount line
  changes).
- **The manuals' genre flips** — both capture; flip to define by logged decision
  once the ground proves stable under the system's use.
- **The first-slice judgment point** — the thinker-role question's last formal
  check, expected to confirm steward-only (`knowledge/knowledge.log.md` 0006).
- **The README delta + flow-back** — both owed at *system bootstrapped*'s exit.

**Next:** system bootstrapped — opening with its stack evaluation.