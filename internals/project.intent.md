# project.intent.md

> **Genre: define.** Why this project exists, for whom, and what "done" means — the *why*
> upstream of the definition's *what*. Settled at landing; revised only by decision,
> recorded in the problem log.

## Why this project exists

Two purposes, one project:

1. **Build the service** — idempotent order creation: the sole authority on whether a
   purchase intent has already become an order, and on making it one — once. The
   authoritative *what* is the system definition (drawn with the five-layer model); this
   doc never restates it.
2. **Ground the method** — this is the project that proves the correctness-driven
   single-service method on real work: it fills the idempotency flow walkthrough from
   lived slices and settles the open **prove/verify** question (whether evidence deserves
   its own stage).

## For whom

The project's surface is written for a **reviewer assessing engineering depth** — a
hiring manager, a senior engineer, a peer — who gives it minutes, not hours. What they
must be able to judge in those minutes: what this system guarantees can never go wrong,
against what adversity, and where the proof runs. The surface serves that reader;
everything deeper is a link into code and evidence, per the adopted projection rules.

## Portfolio role

**Project 1 of a two-project portfolio arc:** within-service correctness — one service as
the L2 system of interest, exercising L1–L4 of the five-layer model. Project 2 (future, a
system of services) takes the **cross-service scope**: the L5 plane — consistency,
coordinated failure, ordering and idempotency *between* services. The seam between the
projects is the **L4/L5 line made into a project boundary**: local correctness proofs do
not compose for free; project 2 exists to work exactly what this project's correctness
cannot guarantee.

Within that role, the visible product is the **guarantee depth**: named invariants under
a named adversity model, decomposed into strategy-free guarantees, enforced by design,
demonstrated by evidence. Feature-thin by design; the evidence is the substance.

## Boundary, by design

Anything **cross-service** is out — that is project 2's subject. Also deferred by the
definition: expiration and time-based behaviour. If a concern turns out to need another
service to be correct, it belongs to a later project, not here.

## Success shape — "done" is checkable

The project is done when every statement below holds:

- every slice in the registry is **closed on passing evidence** — the adversity created,
  the invariant watched surviving it;
- the **guarantee set is legible as the project's face**: the public surface shows each
  demonstrated invariant, its adversity, its guarantees, and how to run the evidence;
- the **method walkthrough is filled** from the lived slices, the prove/verify question
  answered, and the corrections **delivered back** to the workbench as flow-back;
- the node is **closed out clean** per its map's finalization: projection's polish pass
  run, reusable material distilled upstream, the record at rest.