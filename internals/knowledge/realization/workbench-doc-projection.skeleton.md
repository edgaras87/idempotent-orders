# workbench-doc-projection.skeleton.md

> **A workbench skeleton: the reusable starting picture of a project's doc projection.**
> A skeleton is not a model (it may carry wholly unlived shape, marked as such) and not a
> live artifact: a correctness-driven backend project **instantiates** it — copies the
> picture, trims, extends, redraws freely — as its **own** projection rules; the skeleton
> is never the project's master. What a project actually projects, and when, is that
> project's own decision (a setting, stated in its vision). **Unlived** — shipped as shape
> so no project starts from zero; the first projecting project corrects it, and settled
> parts graduate to a model only after lived use.

---

## The pipeline, and the one rule that binds it

Internal truth projected outward, directed:

```text
definition / vision → internal docs → public/ docs → README
```

The public surface is **derived from internals and never a second master**: internal truth
leads, projection follows; a lagging projection is refreshed or cut, never patched
independently.

## What the surface is for — one job

For a correctness-driven backend, the public surface exists to make **guarantee depth
visible to a reviewer in minutes** — the method's inversion applied to docs: not what the
system does, but *what can never go wrong, and the proof*. Every content choice a project
makes tests against that.

## What projects (internal plane → public plane)

The starting picture; a project trims or extends per its own surface:

| Internal truth | Projects as | How much |
|---|---|---|
| definition, L2 statement | README's "what this is" | one paragraph, no parts named |
| definition's concern map | README's invariant list (demonstrated / planned) | one line per invariant |
| correctness spec + slice docs | one public write-up per slice | ~1 page: invariant, adversity, guarantees, enforcement + why, evidence + how to run |
| the method | README's method note | two lines + a link out, never restated |
| evidence tests | "run it yourself" | commands only |

## What never projects

The node's self-knowledge — vision, log, status, worklog, lenses, workbench knowledge. The
surface projects **system truth, not process truth**. (A portfolio surface *might* earn one
short "how this was built" note — undecided; the first projection judges it.)

## How much — the depth rule

The README stays scannable: a reviewer decides in about a minute whether to click into a
slice. A slice write-up is self-contained at roughly a page. Anything deeper is a link into
code and tests, never more prose. Prose thickness is capped; substance grows by **count of
demonstrated invariants**, not document length.

## When — cadence

**Cadence is the project's own decision** — a setting, stated in the project's vision, never
dictated here. The starting picture, shaped for the portfolio-facing case: **event-driven
projection at the realization map's own exits** (`workbench-backend-realization.map.md`),
thickness growing with substance:

- **at *working ground prepared*** — the thin README, the whole surface: the L2 paragraph,
  the method note, stack, an honest status line;
- **at each *slice completed*** — the slice's public write-up (the `document` stage pointed
  outward, nearly free because the substance is frozen); the `public/` plane is earned at
  the first of these; the README grows its demonstrated-invariants list;
- **at *finalization*** — one polish pass: README as the finished front door, consistency
  across write-ups, stale status lines removed.

A project with no reader during progress may trim this to the finalization pass alone.
Projection is never calendar-driven — the realization map answers *when*; only
just-become-true, won't-change substance is projected, keeping sync cost near zero.

## Open questions the first projection must answer

- Does cadence really split by audience, or by projection thickness?
- What must a portfolio surface show beyond invariants and evidence — and what must it
  refuse to show?
- Is projection a `document`-stage byproduct in practice, or its own act?
- Does projection stay with the thinker (where the realization skeleton's narrow-roles
  hypothesis folds it), or does it drift to the steward in practice?

---

## Lifecycle note

Skeleton v1, wholly unlived — shipped as a starting picture, not distilled practice. Each
projecting project's finalization owes this doc a correction pass; graduation of any part
into a model happens only after lived use, through the workbench steward.
