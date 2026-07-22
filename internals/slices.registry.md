# slices.registry.md

*The execution phase's living control point. Rewritten freely as slices move — no
genre; its reliability is that it is current. Every standing change's why lands in
`log.md`; what each concern *is* stays mastered in `system.definition.md` (L4) — this
doc never restates it, only names, one-line invariants, standing, and evidence.
Exactly one slice is chosen-next at any time.*

## Standing vocabulary

- **candidate** — on the map, not yet worked.
- **chosen-next** — the one slice queued for `specify-correctness`; the choice's why
  in the problem log.
- **in-progress `(stage)`** — being worked; annotated with the method's current stage:
  `spec`, `plan`, `write`, or `document`.
- **closed-on-evidence** — the completion test met: the adversity-creating evidence
  exists and passes. Terminal.

## Slices

| id | slice (definition concern) | invariant, one line | adversity | standing | evidence |
|---|---|---|---|---|---|
| S1 | Duplicate suppression (L4.1) | N identical requests → exactly one persisted order | duplicate delivery | **chosen-next** | — |
| S2 | Concurrent-duplicate race (L4.2) | the S1 invariant holds when identical requests race at the same instant | contention | candidate | — |
| S3 | Safe response replay (L4.3) | every retry of a created intent gets an answer consistent with the one order | duplicate delivery (response side) | candidate | — |
| S4 | Crash during creation (L4.5) | a mid-creation death leaves a state the retry converges to one order from | partial failure (creation path) | candidate | — |

**Working order** *(expectation, not commitment — re-decided at each close, in the
log)*: S1 → S2 → S3 → S4.

## Notes

- **S1 absorbs the request-identity contract (L4.4).** Concern 4 is not a slice: it
  carries no invariant-under-adversity of its own — it is the contract the other
  invariants are defined against, and its correctness (key scope, validation,
  collision behaviour) surfaces as guarantees of S1's invariant. It is worked as the
  opening of S1's `specify-correctness`. Folded, not dropped — this line is the
  reconciliation: definition concerns 1–5 ↔ slices S1–S4 + this fold.