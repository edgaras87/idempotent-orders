# workbench-backend-realization.map.md

> **A workbench map — a reusable skeleton, not a live map.** A project **adopts**
> this at its birth: the fork copy itself serves as the project's map, molded in place
> under the project's fork rules; a separate live map artifact is born only by need
> (see *Map adoption* below) — the skeleton is never any project's master.
>
> **Map discipline — the three-way execution-state division.** Realization tracking splits
> into three responsibilities that must not collapse into each other: the **map** is
> orientation only — phases as outcomes with exit conditions, redrawn by decision, no
> checkmarks, no execution state; the project's **present-state snapshot** (its status)
> is the single master of *where we are*; the **worklog** (born at the first executed
> step) is the append-only record of what actually happened. The record couples to the
> map by **shared step and phase names, never by reference-to-read**: every record
> entry is self-contained — a future reader needs no map open to understand what was
> decided or done. The map yields to the worklog when reality corrects it, never the
> reverse. Which artifacts and kinds carry these is the project's own vocabulary.

---

## The flow

```text
initialization → execution → finalization
```

- **initialization** — everything needed before project work can run: the node, the
  problem's reasoning chain, the working ground.
- **execution** — the planned work itself: for a correctness-driven backend, the
  definition's concern map worked slice by slice through
  `specify-correctness → plan → write → document`
  (`workbench-correctness-driven-backend.model.md`).
- **finalization** — closing up after the goals are met: evidence and docs made legible,
  reusable material distilled back upstream, the node brought to rest.

## Initialization — the step pattern

Outcome-steps in rough dependency order, each with the exit condition that makes it *left
behind*. A project copies, trims, or extends these at adoption:

1. **Node bootstrapped.** Vision, log, status, steward — the node runs by its rhythm.
   *Exit: sessions proceed without re-deciding the basics.*

2. **Prepare-thinking-part.** *(unlived — a composite step grouping the problem's
   reasoning chain: one nature of work, the thinker's territory under the narrow-roles
   hypothesis below; adopted from one project's decided-but-not-yet-lived
   initialization, owed confirmation at that project's finalization correction pass.)*
   Its inner steps, in order:

    - **project-intent** *(unlived)* — why the project exists, for whom, and what "done"
      means: purpose, audience, success criteria, deliberate tradeoffs — the *why*
      upstream of the definition's *what*. Intent never describes what the system is or
      does — that is the definition's job.
      *Exit: downstream scope and cadence questions are answerable by reference, not
      re-argued.*
    - **system-definition** — what is being built, authoritatively stated
      (`definition` kind; for a backend, drawn with
      `workbench-five-layer-system.model.md`); reasoning conventions adopted.
      *Exit: the definition is mastered and referenced by the project's vision; its
      concern map names the candidate slices.*
    - **slice-registry born** — the definition's concern map made actionable: the
      registry populated with the candidate slices and their standing, the first slice
      marked chosen-next — the ordering decision the definition deliberately does not
      make, with its why in the project's record. The *living* registry remains the
      **control point of the execution phase** (chosen-next, closed-on-evidence); only
      its birth sits here.
      *Exit: the registry exists, populated, with one slice chosen-next.*

   *Phase-step exit: the reasoning chain stands — intent, definition, registry — and
   the first slice is named.*

   *Open questions the first lived pass must answer:* does folding the first-slice
   choice into registry-birth survive lived work, or does the choice deserve its own
   step again? Does the grouping principle — map steps mirroring role territory —
   generalize?

3. **Working ground prepared.** The repo ready for real work: layout settled (node beside
   code), stack and tooling chosen, an empty-but-running service skeleton, an evidence
   harness able to run one adversity-generating test end to end.
   *Exit: a trivial test runs green; the chosen slice has somewhere to land.*

**Phase exit — into `execution`:** the chosen slice's `specify-correctness` can start
with nothing missing around it. The **worklog** is born at the first real executed step.

## Map adoption — need-driven, not scheduled

*(unlived — replaces the former scheduled "realization mapped" step; owed confirmation
at the proposing project's finalization pass.)*

A project **adopts** this skeleton as its map by working against its fork copy directly,
molding it in place under the project's fork rules — grouping, trimming, extending,
each molding a logged fork deviation. Execution state never enters the copy: position
and history live entirely in the project's record, coupled by names per the map
discipline above. A **separate live map artifact** is born only when project-specific
redrawing — lazy zoom on the current phase, cut steps, project-only reordering that is
no knowledge claim — would pollute the fork's diff against upstream; that birth is a
project decision at its moment of need, never an initialization step.

## Execution — known shape only

Slices, each carried to evidence through the method's flow; order and count belong to the
project's own zoom when the phase is entered.

**Narrow-roles hypothesis (unlived — travels here, not in the models):** the project
steward delegating to context-trimmed roles. The steward stays the **thin custodian** — it
carries the record, not all content; asked *what next, and with which role*; plausibly also
the **context-preparer**, gathering per-slice only what the next role needs. The roles,
plausibly:

- **thinker** — the problem's one reasoning role: it owns the prepare-thinking-part
  chain (intent, definition, registry-birth); keeps the **slice-registry** — the
  **control point of the execution phase** (chosen-next, closed-on-evidence), carrying
  the *where-we-are* of slices with the project's status stating it — the division
  otherwise unchanged (the map orientation-only, the worklog the happened-record);
  selects each slice and closes it on its evidence; and **projects internal truth to
  the public surface** (`workbench-doc-projection.skeleton.md` the starting picture).
  Whether projection drifts to the steward in practice is an open question the first
  project answers;
- **infrastructure** — the governed ground: environment, then required services (input:
  the registry reasoning); writes **manuals for two audiences** — the coder (handoff: what
  services exist, how to use them) and the human reader (set up and use everything
  manually); re-entered on new dependencies. Candidate refinement of the
  working-ground step: infrastructure before bootstrap, so bootstrap wires up to real
  services and validates the ground;
- **slice-reasoning** — possibly its own role: an isolated **correctness-construction**
  stage — the spec built in a context-trimmed session fed by the steward's prepared
  context, rather than inside the thinker;
- **coder** — plan → build → validate; owns the enforcement choice; receives the
  correctness spec plus a ready ground: environment and service knowledge, a bootstrapped
  project, the project's conventions.

To be confirmed, corrected, or discarded by lived work — then modeled.

## Finalization — known shape only

Evidence and guarantees made legible as the visible product; what proved reusable (map
skeleton corrections included) distilled back upstream; the node closed out.

**Doc projection (starting picture: `workbench-doc-projection.skeleton.md`, unlived):**
internal truth projected to a public surface — `definition/vision → internal docs →
public/ docs → README` — derived from internals and **never a second master**. What
projects, what never does, how much, and the exit-anchored cadence are the skeleton's
starting picture, adopted by the project as its own projection rules; **cadence is
the project's own decision**, stated in its vision. Runs through the **thinker** (the
narrow-roles hypothesis folds projection into it); confirmed, corrected, or discarded by
the first projecting project.

---

## Lifecycle note

Skeleton v1 amended by its first adopting project's fork (the deviation and its why:
that fork's `fork.log.md`, entry 0001): the reasoning chain grouped as
`prepare-thinking-part` (intent, definition, registry-birth — folding the former
first-slice step), and scheduled map instantiation replaced by need-driven adoption;
both unlived, owed confirmation at that project's finalization correction pass. Each
project's finalization owes this doc a correction pass — that is how the skeleton earns
generality.
