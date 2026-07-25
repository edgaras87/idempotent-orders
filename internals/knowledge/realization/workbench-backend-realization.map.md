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
preparation → execution → finalization
```

- **preparation** — everything needed before project work can run: the node, the
  problem's reasoning chain, the infrastructure, the born system.
- **execution** — the planned work itself: for a correctness-driven backend, the
  definition's concern map worked slice by slice through
  `specify-correctness → plan → write → document`
  (`workbench-correctness-driven-backend.model.md`).
- **finalization** — closing up after the goals are met: evidence and docs made legible,
  reusable material distilled back upstream, the node brought to rest.

## Preparation — the step pattern

**Where the map begins:** after birth. The mechanical birth — repo created, git ground
earned, the seed copied in, the session vehicle set up — is the workbench birth guide's
territory: human-only, pre-session, upstream of this map. Step 1's work is what comes
after: making the born node actually *run*.

Outcome-steps in rough dependency order, each with the exit condition that makes it *left
behind*. A project copies, trims, or extends these at adoption. *(Their carving mirrors
role territory under the narrow-roles hypothesis below — thinker, infrastructure, coder —
the grouping principle of the first adopting project's fork, extended across the phase's
tail; whether it generalizes further stays an open question.)*

1. **Node bootstrapped.** Vision, log, status, steward — the node runs by its rhythm.
   *Exit: sessions proceed without re-deciding the basics.*

2. **Problem framed.** *(unlived — a composite step grouping the problem's
   reasoning chain: one nature of work, the thinker's territory under the narrow-roles
   hypothesis below; adopted from one project's decided-but-not-yet-lived
   preparation, owed confirmation at that project's finalization correction pass.)*
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

3. **Remote established.** *(human-only, mechanical — no session work; lived once by
   the first adopting project, which created its remote late, mid-preparation, and
   placed the step here from that lesson.)* The repo gets its private off-machine
   home: the project's name **confirmed against the framing** (a birth name may be
   provisional; rename now, while it's local and free), description projected from
   the thin README (owed at *problem framed*'s exit under an exit-anchored
   projection cadence), remote created, history pushed. Before *problem framed* the
   local repo suffices — the project may still dissolve or be renamed at zero cost;
   after it, the project is committed-to, named for sure, and about to grow code.
   Private by default; publishing is a separate decision, never this step's.
   *Exit: the remote exists, private, under the confirmed name, current.*

4. **Infrastructure established.** *(lived once — promoted from the narrow-roles
   hypothesis' candidate refinement: infrastructure before the system's bootstrap, so
   the bootstrap wires up to real services and validates the ground; the infrastructure
   role's territory.)* The governed ground the system will run on: the environment
   chosen and running, then the services the problem's reasoning chain requires —
   evaluated from problem framed's outputs (the definition's environment reasoning, the
   registry's reasoning), **constrained by need**, never provisioned ahead of it.
   Manuals written for two audiences: the coder (what services exist, how to use them)
   and the human (set up and use everything manually). Re-entered on new dependencies
   during execution.
   *How: `workbench-infrastructure-establishment.guide.md` — the walked sequence,
   lived once, owed confirmation by the next establishing project.*
   *Exit: the chosen environment runs; the services the reasoning chain requires are
   up, constrained to need; both manuals exist.*

5. **System bootstrapped.** *(re-carved from the former working-ground step — it is the
   system being built that is born here; the coder's territory.)* The system brought to
   life on the established infrastructure: stack and tooling chosen and initialized —
   the system's name, description, starting dependencies; layout settled (node beside
   code); the project's conventions and baselines stated where the work demands them
   (folder layout, naming, recurring code patterns); an empty-but-running system
   skeleton wired to the real services; an evidence harness able to run one
   adversity-generating test end to end.
   *Exit: the system skeleton runs wired to the real services; layout and conventions
   settled; a trivial adversity-generating test runs green; the chosen slice has
   somewhere to land.*
   *A lived, stack-specific ready sequence:
   `workbench-spring-boot-bootstrap.walkthrough.md` (Spring Boot on an established
   PostgreSQL ground) — applies only where the step's own stack evaluation lands on
   Spring; other stacks distill their own at their first lived pass.*

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
project decision at its moment of need, never a preparation step.

### Steps and how-docs — how the map couples to knowledge

*(lived once — first exercised at the infrastructure step's exit: the guide born from
the lived walk, then the step's pointer added; owed further confirmation with the
rest.)*

The map carries *what steps and when*; where the **how** of a step is distilled, it
lives as a knowledge doc in `realization/` and the step **points** at it (as
*system-definition* points at the five-layer model). The coupling rules:

- **A step needs no how-doc to run.** With none, the how is worked out live, in
  session — that *is* the first lived pass; nothing is missing.
- **The record documents the process — never a separate narrative.** Outputs plus the
  problem log plus the worklog are the process documentation; a "how we did the step"
  doc beside them would duplicate the record.
- **After a lived step, one test: did a generalizable method emerge?** No — nothing is
  born; the record holds the story; the common ending. Yes — a how-doc is born in
  `realization/` (a fork addition, logged, a flow-back candidate), and only then the
  step gains its pointer.
- **Direction of dependency: lived work → knowledge → pointer.** Never a map slot
  awaiting a doc, never a how-doc authored ahead of the living.

## Execution — known shape only

Slices, each carried to evidence through the method's flow; order and count belong to the
project's own zoom when the phase is entered.

**Narrow-roles hypothesis (unlived — travels here, not in the models):** the project
steward delegating to context-trimmed roles. The steward stays the **thin custodian** — it
carries the record, not all content; asked *what next, and with which role*; plausibly also
the **context-preparer**, gathering per-slice only what the next role needs. The roles,
plausibly:

- **thinker** — the problem's one reasoning role: it owns the problem-framing
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
  manually); re-entered on new dependencies. *Its preparation territory is the
  `infrastructure established` step — the former candidate refinement (infrastructure
  before bootstrap, so bootstrap wires up to real services and validates the ground),
  promoted into the step sequence by the first adopting project's fork; its walked
  how: `workbench-infrastructure-establishment.guide.md`;*
- **slice-reasoning** — possibly its own role: an isolated **correctness-construction**
  stage — the spec built in a context-trimmed session fed by the steward's prepared
  context, rather than inside the thinker;
- **coder** — plan → build → validate; owns the enforcement choice; receives the
  correctness spec plus a ready ground: environment and service knowledge, a bootstrapped
  system, the project's conventions. *Its preparation territory is the `system
  bootstrapped` step.*

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

Skeleton v1, amended in place by its first adopting project's fork — the deviations
and their why live in that fork's `fork.log.md` (entries 0001–0005, 0008, 0010), which
is the change history; this note carries only standing. Lived by that project so far:
the *problem framed* composite step and its inner chain, need-driven adoption and
the steps-and-how-docs coupling (first exercised at the infrastructure step's exit —
the guide born, then the pointer), the molded vocabulary, the *infrastructure
established* step itself, the *remote established* step (placed from the lived
lesson of creating the remote late), and the *system bootstrapped* re-carve —
now lived by that project's first pass (its Spring-stack ready sequence distilled:
the walkthrough above). All amendments are owed confirmation, correction, or reversal at that
project's finalization correction pass. Each project's finalization owes this doc a
correction pass — that is how the skeleton earns generality.