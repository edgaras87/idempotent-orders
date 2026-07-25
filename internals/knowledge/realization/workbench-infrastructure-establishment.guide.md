# workbench-infrastructure-establishment.guide.md

> **A workbench guide: how the *infrastructure established* step is walked** — from no
> infrastructure to a governed, verified ground the system bootstrap can wire to. A
> guide is a walkable *how* for a recurring piece of work — not a model (no claim
> about what things are), not a manual (no single project's operating truth); audience:
> any role or human walking the step. Genre: **capture** — **lived once** (by the
> first walking project — its worklog is the happened-record); **owed confirmation,
> correction, or generalization by the next establishing project** — deviations a
> different ground forces are exactly the evidence this guide's next revision wants.
> Generic throughout; a walking project's concrete choices land in its own record and
> manuals, never here.

---

## Inputs, before the first move

The reasoning chain's outputs, standing: the definition's **L1** (the environment
facts the ground must honor) and the **registry's slices** (whose adversities are the
deciding constraints — evidence needs drive infrastructure, not habit). If these
aren't settled, the step isn't ready.

## The walk

1. **Decide the Execution Environment.** Weigh candidates against the slices'
   adversity needs first (what must the evidence be able to *do* — kill processes,
   race transactions, survive restarts?), then L1's runtime facts, then operability
   (the ground must stay fully manual-operable — a manual is owed). Name the
   alternatives weighed. Log the decision with its why. Then stand the environment
   up — and **birth the operator manual with its environment section written from
   the lived setup**, contemporaneously, not reconstructed later; the worklog is
   born here if this is the project's first executed step.

2. **Evaluate Infrastructure Services — constrained by need.** Slice by slice: what
   capability does each invariant's evidence actually require? The service set is
   what survives that question — and **the not-provisioned list is stated with each
   exclusion's why** (a cache refused, a queue refused, and for what reason). One
   log entry; anything joining later re-enters this evaluation as a logged decision.

3. **Name each service's constraints.** Requirements and limitations imposed on a
   service — governance of the ground, stated as principles (e.g. *the running
   application must not control database structure*), deliberately **not** registry
   invariants: they are no properties of persisted state under adversity. Each
   constraint names its enforcement mechanism.

4. **Apply — or birth — the governing knowledge.** Where a constraint's realization
   is reusable across projects, it is knowledge, not project truth: born (or
   applied, if it exists) in `realization/` as a fork addition, generic, with the
   project's concrete wiring kept in its manuals. Worked example:
   `workbench-postgres-role.model.md` — the authority split a datastore constraint
   realizes.

5. **Stand the services up, and verify both ways.** Ground files land as project
   truth (declarations, bootstrap scripts, tool configs — secrets split out per the
   project's credential handling); the services come up on the environment. Then
   **two complementary verifications**: the **catalog check** (the ground's state
   queried against the governing knowledge's claims, expected results stated) and
   the **behavioral check** (the constraint *attempted* and watched being refused —
   the governing rule demonstrated live, not assumed). The operator manual grows
   its service sections from this lived work.

6. **Write the coder handoff.** The second manual, for the role building the system:
   what services exist, how to reach them (inside vs. outside the environment's
   network), **which identity to connect as and which never to use**, what the
   constraints allow and refuse, how schema changes are made. It masters the
   step's vocabulary. The step's exit needs both manuals standing.

7. **Run the exit test.** The map's steps-and-how-docs test against the lived walk:
   did a generalizable shape emerge beyond this guide? Findings that aren't
   structure ride the project's flow-back batch as notes.

8. **Distill service walkthroughs.** Per service stood up, one test: **was the
   setup long, sequenced, and likely to recur in later projects?** If yes — a
   **walkthrough** is born in `realization/` as a fork addition: the service's
   end-to-end *do this*, scripts included with their recall comments, project
   names as placeholders, the environment context flagged where it binds — so no
   later project re-derives or even re-assembles the same sequence. Discipline:
   the walkthrough **masters nothing** — governing claims stay with their model
   (named in the walkthrough's header; on conflict the model wins, corrections
   flow model → walkthrough); it is owed confirmation by its second using
   project. If no — nothing born; the manuals and the record suffice. Worked
   example: `workbench-postgres-setup.walkthrough.md`.

## Vocabulary (the step's own, mastered by the walking project's coder manual)

- **Execution Environment** — the local runtime host for infrastructure.
- **Infrastructure Service** — a capability provided to the system.
- **Infrastructure Service Constraint** — a requirement or limitation imposed on an
  infrastructure service.

---

## Lifecycle note

Lived once, by the first walking project (its worklog the happened-record). Owed
confirmation by the next establishing project — especially where its ground differs:
no containers, several services, other constraint families. Corrections arrive as
ordinary updates, decided and logged at the node that masters this doc — the log,
not this note, is the change history.
