# workbench-correctness-driven-backend.model.md

> **A workbench model in three parts: what a correctness-driven backend is** (the
> concept), **how one thinks in this discipline** (the way of thinking), and **how the
> work runs** (the way of working) — a recognized division of knowledge: declarative
> (what it is), heuristic (how to reason), procedural (how to act). The parts share one
> law: **anatomy lives in Part I
> (what must exist), reasoning in Part II (the moves that produce it), flow in Part III
> (when each move runs)** — each fact in its part, the others referencing it, never
> restating.
>
> **Provisional, and grounded narrowly.** Extracted from three *single-service* examples;
> confirmed there, open for the multi-service case until a real project of that shape
> tests it (see the horizon note). The first born project grounds the single-service
> method.

---

# Part I — The concept: what a correctness-driven backend is

A backend whose primary deliverable is a set of **explicitly stated invariants** together
with **demonstrated evidence that they hold under an explicit adversity model**. Features
exist as vehicles for the invariants, not the reverse.

Its anatomy — five ingredients, each a thing that must *exist*, written down:

1. **Named invariants** — properties of persisted state that must never be violated
   ("stock is never oversubscribed"; "one idempotency key → exactly one persisted
   order").
2. **A named adversity model** — precisely what threatens each invariant.
3. **Required guarantees** — the invariant decomposed into the strategy-free
   sub-properties that must all hold for it to survive the adversity (e.g. "one order per
   key" decomposes into uniqueness, concurrent winner/loser resolution, safe replay,
   key-scope correctness). Still **what must be true, never how**.
4. **Enforcement by design** — the mechanism that makes violation impossible or reliably
   detected: the correctness-by-construction discipline (strong types, invariants
   enforced at construction, atomic conditional updates, explicit state machines,
   constraints as the last line of defence).
5. **Evidence** — a test that *creates* the adversity (concurrent hammering, injected
   duplicates, kill-mid-transaction) and shows the invariant surviving, guarantee by
   guarantee. The highest-value ingredient, and the one ordinary projects skip.

*(Where an invariant comes from when a whole system is analyzed: an L4 local problem of
`workbench-five-layer-system.model.md` — reference, not restatement.)*

# Part II — The way of thinking

The moves of mind that produce Part I's ingredients; each is a discipline, violated
quietly if not named:

- **The inversion.** Feature-driven asks *what should it do?* first and bolts correctness
  on as a quality attribute. This discipline asks ***what must never happen?*** first;
  the feature set is whatever is minimally needed to make the guarantees real. Corollary:
  the surface stays deliberately **feature-thin** — guarantee depth is the visible
  product, and feature-creep is how the correctness theme dies quietly.
- **Correctness is relative to adversity.** "Correct" is meaningless until you state what
  it is correct *against*. Every claim of safety names its threat.
- **The guarantee challenge.** Guarantees are **derived by attacking the invariant** —
  *"what would let this hold on paper yet be violated in fact?"* — **never looked up**.
  This is why the guarantee layer is the real, non-textbook work even on a known problem:
  the examples everyone knows show endpoints (invariant → mechanism); the span between
  them is where the thinking happens.
- **What before how — and kept apart.** A guarantee states what must be true; enforcement
  states how. Fusing them is the classic corruption: a mechanism named early masquerades
  as a property, and alternatives are never weighed against the actual requirement.
- **Evidence over trust.** Construction is never believed on inspection: the adversity is
  *created* and the invariant watched surviving it. A guarantee without an
  adversity-generating test is a claim, not a result.

# Part III — The way of working

**The slice — the work unit.** *One invariant made real*: a single invariant–adversity
pairing carried through the whole flow to demonstrated evidence — via the required
guarantees that preserve it (one invariant, typically many guarantees). Completion test:
the evidence exists and passes.

**The execution flow (provisional):**

```
specify-correctness → plan → write → document
```

- **specify-correctness** — name the invariant(s) and adversity model, run the guarantee
  challenge (Part II), set evidence criteria; produce the **correctness spec**
  (invariant(s) → guarantees → requirements, plus adversity and evidence).
  **Strategy-free** — the what/how separation (Part II) enforced as a stage boundary.
  *Why first:* nothing downstream has meaning until what-must-hold and
  what-it-holds-against are explicit. The slice's first handoff.
- **plan** — choose the enforcement mechanism that satisfies the spec's guarantees
  (optimistic vs pessimistic locking, unique constraint, idempotency key, state machine
  with guarded transitions, outbox, …), and say why it defeats *that* adversity. The
  first stage that touches *how*.
- **write** — implement the enforcement, and the evidence tests that generate the
  adversity.
- **document** — record the invariant, its guarantees, how they are enforced, and the
  evidence — the guarantee set legible for recall, reading as the work's visible
  substance.

> **Open — does evidence deserve its own stage?** Specified in `specify-correctness`,
> realized in `write`; whether a distinct **prove/verify** stage sits between `write` and
> `document` is settled from a real service walkthrough, not asserted here. Per-stage
> **method docs** (e.g. a coding-style doc for `write`) are written when the work demands
> them.

**Worked demonstrations — the three seeding services**, an **adversity ladder** (one
added adversity dimension per rung; the third effectively contains the second — the
ladder is why the three teach the method well: each isolates one adversity so the flow is
legible on it). Per-service flow walkthroughs are **deliberately absent**: they are
filled from lived work, never invented here.

- **Inventory Reservation** — *contention*. Invariant: `reserved ≤ available` under
  concurrent lifecycle operations. Enforcement (candidate): atomic conditional update /
  row-level locking / isolation choice.
- **Idempotent Order Creation** — *duplicate delivery*. Invariant: N identical requests →
  exactly one persisted order. Enforcement (candidate): idempotency key + unique
  constraint + safe replay.
- **Order Lifecycle Under Retries** — *time and partial failure*. Invariant: only legal
  transitions ever occur; recovery converges. Enforcement (candidate): explicit state
  machine, transition guards, crash-recovery reasoning, outbox.

Each example compresses invariant → enforcement in one line — the guarantee layer is
elided; per Part II, that middle is the work.

**From one service to a system (deferred horizon):** across service boundaries the
adversity class changes — consistency across services, coordinated failure, sagas /
compensations, cross-service ordering and idempotency. Confirmed single-service; the
multi-service case will confirm, correct, or extend flow and taxonomy when a project of
that shape exists. Not the first project's concern — noted so the method's boundary is
explicit.

# Vocabulary

- **invariant** — a property of persisted state that must never be violated; named,
  written. *(concept)*
- **adversity / adversity model** — what threatens an invariant; a provisional
  taxonomy, expected to grow: contention, duplicate delivery, partial failure/crashes —
  later reordering, clock skew, cross-service inconsistency. *(concept)*
- **guarantee** — a strategy-free correctness property preserving an invariant; produced
  by the guarantee challenge; distinct from enforcement. Do **not** use "guarantee" to
  mean "a made-real invariant" — that conflates this layer with the slice. *(concept,
  via Part II's move)*
- **enforcement** — the mechanism; downstream of guarantees, chosen at `plan`.
  *(concept/working seam)*
- **evidence** — the adversity-creating test; a slice's completion test.
  *(concept/working seam)*
- **slice** — one invariant made real. *(working)*
- **correctness spec** *(provisional name)* — invariant(s) → guarantees → requirements,
  plus adversity + evidence criteria; strategy-free; produced by `specify-correctness`,
  consumed by `plan`. *(working)*
- **adversity ladder** — the escalation reading of a slice set. *(working)*

---

## Lifecycle note

Provisional. The first born project grounds the method: it fills the idempotency
walkthrough from lived work and is where the prove/verify question gets settled.
The multi-service extension stays a deferred horizon. Corrections arrive as ordinary model
updates, logged at the workbench.
