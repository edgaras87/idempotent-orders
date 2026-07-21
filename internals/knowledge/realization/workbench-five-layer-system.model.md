# workbench-five-layer-system.model.md

> **A workbench model: how to think about a software system.** **Provisional** until
> confirmed through use on real projects.
>
> **Boundary up front:** the model reasons about the *software system being built* — never
> about repo or node structure, implementation, or how work is executed. It defines how to
> *think* about a system, nothing more.

---

## Purpose

A system is not a single unit, and reasoning that treats it as one goes wrong in a specific
way: claims that are true at one level get applied at another. The model prevents that by
fixing **five layers, each a distinct kind of concern**, with hard separation rules between
them. Every statement about a system belongs to exactly one layer; an argument that drifts
across layers without saying so is malformed.

The model governs *understanding* only. Implementation, technology choices, and all execution
concerns — how work is selected, ordered, sliced, tracked — are deliberately outside it (they
belong to the execution flow in `workbench-correctness-driven-backend.model.md`).

---

## The layers

```
L1  Environment            the world the system lives in — outside its control
L2  System of Interest     the thing being built: its responsibility, boundary, authority
L3  Responsibility Areas   the system's internal divisions by ownership
L4  Local Problems         bounded problems inside one area: invariants under adversity
L5  Interaction            what emerges when areas interact; correctness across them

containment:  L1 surrounds L2 · L2 contains L3 · each L3 area contains its L4 problems
              L5 is not "below" L4 — it lives between L3 areas
```

### L1 — Environment

The external world the system exists in: external systems, upstream and downstream systems,
the actors that interact with it. The environment **provides inputs and constraints** and
defines what is *outside the system's control*. It never defines the system itself or its
internals — and it never holds the system's authority: a constraint from outside is a fact to
absorb, not a decision-maker inside.

### L2 — System of Interest

The system being defined, analyzed, or built — as one whole. L2 owns the system's
**responsibility** (what outcomes it must produce), its **boundary** (what is inside vs
outside), and its **authority** (which decisions and which state are *its* to own). L2
deliberately says nothing about internal division or specific problems: a correct L2
description reads complete while naming no parts.

### L3 — Responsibility Areas

The system's internal divisions, drawn by **ownership**: which area owns which state and which
decisions. L3 establishes internal boundaries and assigns ownership — nothing else. It does not
describe the environment, does not enumerate problems, does not describe how areas talk.

### L4 — Local Problems

Bounded problems belonging to **a single responsibility area**: its failure conditions, the
**adversities** acting on it, and its **local correctness targets**. L4 is where correctness
work gets its input — in the method's terms, an L4 problem is exactly what
`specify-correctness` consumes and turns into a correctness spec: invariant(s) + adversity +
evidence criteria. L4 never defines system structure, never reaches across a boundary, and
never says anything about work sequencing.

### L5 — Interaction / Composition

The behavior that **emerges when responsibility areas interact**: coordination between areas,
cross-boundary flows, system-level consistency. L5 owns **composition-level correctness** — the
constraints that must hold *across* areas — and is where interaction failures become visible:
coordinated and partial failure spanning areas, cross-boundary ordering and idempotency,
consistency at the seams. L5 never redefines local problems and never reassigns ownership; it
takes L3's boundaries and L4's local guarantees as given and asks what still isn't guaranteed.

---

## Scoping is relative

"System of interest" is a *choice made per analysis*, and the layers nest: one analysis may
treat a single service as the L2 system of interest; a later, wider analysis may treat that
same service as an L3 responsibility area of a larger L2 system. Both readings are correct — at
their own scoping. The rule is: **one analysis, one scoping**. Re-scope explicitly when moving
between them; drifting scope mid-argument is the layer-mixing error in its sneakiest form.

---

## Separation rules

1. **Each concern belongs to exactly one layer.** If a statement won't sit in one layer, it is
   more than one statement — split it.
2. **Layers do not collapse.** The system is not a responsibility area; an area is not a
   problem; a problem is not an interaction.
3. **L4 does not guarantee L5.** Local correctness proofs do not compose for free — a system of
   individually-correct areas can still be wrong at the seams.
4. **L1 constrains; it does not command.** The environment is never confused with the system's
   authority.
5. **Execution belongs to no layer.** Work selection, ordering, slicing, tracking live in the
   execution flow, outside this model.

---

## Bridge to the correctness-driven method

The model and `workbench-correctness-driven-backend.model.md` are complementary, not
overlapping: the model says how to *see* the system's structure; the method says how to *work*
it. The mapping:

- **L4 local problem ↔ a slice's input.** Local correctness target ↔ **invariant**; the
  adversities acting on the area ↔ its **adversity model**; the whole L4 problem ↔ what
  `specify-correctness` consumes.
- **L5 composition correctness ↔ across-service guarantees** — the method's "from one service
  to a system" frontier, and the different adversity class that lives only at the seams
  (separation rule 3, seen from the method's side).
- **Birth guidance.** A system definition naturally fills the layers top-down: L1/L2 first
  (environment, boundary, what the system owns), L3 if real division exists, then L4 problems
  as the slice backlog. L5 stays empty for a single-area system — by design.

---

## Model boundary

The model describes **the system itself**. It does not describe how the system is implemented,
which technologies it uses, how work is executed, or how problems are selected and solved over
time — those belong to engineering and execution processes outside it. And once more,
explicitly: it does **not** describe repo structure, nodes, or their docs.

---

## Vocabulary (model-scoped, engineering-side)

- **environment** — L1: the world outside the system's control; source of inputs and
  constraints.
- **system of interest** — L2: the system being built, as chosen for *this* analysis;
  responsibility + boundary + authority.
- **responsibility area (the system's)** — L3: an internal division owning state and decisions.
- **local problem** — L4: a bounded problem inside one area — failure conditions, adversities,
  local correctness targets; a slice's raw input.
- **local correctness target** — L4: what must hold inside one area; the method's **invariant**.
- **composition correctness** — L5: what must hold *across* areas.

---

## Lifecycle note

Provisional at its rich end. The first born project exercises L1–L4 on a real service
and is expected to confirm or correct the layer definitions; L5 stays untested until a
multi-service project exists. Corrections arrive as ordinary model updates, logged at the
workbench.
