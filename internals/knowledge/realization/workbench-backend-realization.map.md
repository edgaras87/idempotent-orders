# workbench-backend-realization.map.md

> **A workbench map — a reusable skeleton, not a live map.** A project **instantiates**
> this at its
> birth: it becomes the starting picture of the project's own map artifact, redrawn freely
> from then on — the skeleton is never the project's master.
>
> **Map discipline — the three-way execution-state division.** Realization tracking splits
> into three responsibilities that must not collapse into each other: the **map** is
> orientation only — phases as outcomes with exit conditions, only the current phase
> zoomed (lazy zoom), redrawn freely, no checkmarks, no execution state; the node's
> **present-state snapshot** is the single master of *where we are*, pointing into the
> map; the **worklog** (born at the first executed step) is the append-only record of
> what actually happened. The map yields to the worklog when reality corrects it, never
> the reverse. Which artifacts and kinds carry these is the project's own vocabulary.

---

## The flow

```text
initialization → execution → finalization
```

- **initialization** — everything needed before project work can run: the node, the
  project's definition, the working ground.
- **execution** — the planned work itself: for a correctness-driven backend, the
  definition's concern map worked slice by slice through
  `specify-correctness → plan → write → document`
  (`workbench-correctness-driven-backend.model.md`).
- **finalization** — closing up after the goals are met: evidence and docs made legible,
  reusable material distilled back upstream, the node brought to rest.

## Initialization — the proven step pattern

Outcome-steps in rough dependency order, each with the exit condition that makes it *left
behind*. A project copies, trims, or extends these at instantiation:

1. **Node bootstrapped.** Vision, log, status, steward — the node runs by its rhythm.
   *Exit: sessions proceed without re-deciding the basics.*
2. **Project intent stated.** *(unlived — adopted from one project's decided-but-not-yet-
   lived initialization; confirmed or cut at that project's finalization correction
   pass.)* Why the project exists, for whom, and what "done" means: purpose, audience,
   success criteria, deliberate tradeoffs — the *why* upstream of the definition's
   *what*. Intent never describes what the system is or does — that is the definition's
   job.
   *Exit: downstream scope and cadence questions are answerable by reference, not
   re-argued.*
3. **Project defined.** What is being built, authoritatively stated
   (`definition` kind; for a backend, drawn with
   `workbench-five-layer-system.model.md`); reasoning conventions adopted.
   *Exit: the definition is mastered and referenced by the vision; its concern map names the
   candidate slices.*
4. **Realization mapped.** This skeleton instantiated as the project's own map.
   *Exit: the status can point into the map to say where the work stands.*
5. **Working ground prepared.** The repo ready for real work: layout settled (node beside
   code), stack and tooling chosen, an empty-but-running service skeleton, an evidence
   harness able to run one adversity-generating test end to end.
   *Exit: a trivial test runs green; a first slice would have somewhere to land.*
6. **First slice chosen.** One concern picked from the definition's concern map — the
   ordering decision the definition deliberately does not make.
   *Exit: the choice and its why are in the project log; the status names the slice.*

**Phase exit — into `execution`:** the first slice's `specify-correctness` can start with
nothing missing around it. The **worklog** is born at the first real executed step.

## Execution — known shape only

Slices, each carried to evidence through the method's flow; order and count belong to the
project's own zoom when the phase is entered.

**Narrow-roles hypothesis (unlived — travels here, not in the models):** the project
steward delegating to context-trimmed roles. The steward stays the **thin custodian** — it
carries the record, not all content; asked *what next, and with which role*; plausibly also
the **context-preparer**, gathering per-slice only what the next role needs. The roles,
plausibly:

- **thinker** — the problem's one reasoning role: it draws the definition (step 3);
  keeps the **slice-registry** — the definition's concern map made actionable: the
  candidate slices and their standing, the **control point of the execution phase**
  (chosen-next, closed-on-evidence), carrying the *where-we-are* of slices with the
  status pointing at it — the division otherwise unchanged (the map orientation-only,
  the worklog the happened-record); selects each slice and closes it on its evidence;
  and **projects internal truth to the public surface**
  (`workbench-doc-projection.skeleton.md` the starting picture). Whether projection
  drifts to the steward in practice is an open question the first project answers;
- **infrastructure** — the governed ground: environment, then required services (input:
  the registry reasoning); writes **manuals for two audiences** — the coder (handoff: what
  services exist, how to use them) and the human reader (set up and use everything
  manually); re-entered on new dependencies. Candidate refinement of step 5: infrastructure
  before bootstrap, so bootstrap wires up to real services and validates the ground;
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
starting picture, instantiated by the project as its own projection rules; **cadence is
the project's own decision**, stated in its vision. Runs through the **thinker** (the
narrow-roles hypothesis folds projection into it); confirmed, corrected, or discarded by
the first projecting project.

---

## Lifecycle note

Skeleton v1, from one project's initialization actually lived and its later phases only
shaped; the intent step (2) is unlived, owed confirmation by the proposing project's
finalization pass. Each project's finalization owes this doc a correction pass — that is
how the skeleton earns generality.
