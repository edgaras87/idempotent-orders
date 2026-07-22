# idempotent-orders

A correctness-driven backend service: **idempotent order creation** — the sole
authority on whether a purchase intent has already become an order, and on making it
one — once.

## Why this exists

This project is built to demonstrate **guarantee depth**, not feature breadth: named
invariants under a named adversity model, decomposed into strategy-free guarantees,
enforced by design, and demonstrated by adversity-creating evidence. It is project 1
of a two-project arc — within-service correctness here; a system of services, and the
correctness that only exists *between* services, in project 2. The service is
feature-thin by design; the evidence is the substance.

## What this is

The service owns the mapping *purchase intent → at most one order*: accepting a
create-order request, deciding new-vs-duplicate, persisting the one order, and
returning a result a retry can rely on. Give it the same purchase intent a hundred
times — concurrently or spread over hours — and there will be one order and one order
only, with every caller answered consistently with that single order. Payment,
inventory, fulfilment, and the order's life after creation are deliberately outside
its boundary.

## Invariants

All currently **planned** — each will be carried to passing evidence as its own slice,
and this list will mark them demonstrated as that happens:

- **Duplicate suppression** — N identical requests → exactly one persisted order
  *(adversity: duplicate delivery)*
- **Concurrent-duplicate race** — the invariant above holds when identical requests
  race at the same instant *(adversity: contention)*
- **Safe response replay** — every retry of a created intent gets an answer consistent
  with the one order *(adversity: duplicate delivery, response side)*
- **Crash during creation** — a mid-creation death leaves a state the retry converges
  to one order from *(adversity: partial failure)*

## Method

Built correctness-first: invariants and adversity are specified before any mechanism
is chosen, and every slice closes only on evidence that creates its adversity and
shows the invariant surviving. The method and the system model behind it live in
[`internals/knowledge/realization/`](internals/knowledge/realization/) — this README
never restates them.

## Status

**Problem framed** — intent, system definition, and the slice registry stand; no code
yet. Next: infrastructure, the system's bootstrap, then the first slice (duplicate
suppression) to evidence.