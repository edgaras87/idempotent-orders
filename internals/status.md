# status.md

*The problem's snapshot. Rewritten freely (at minimum at session end); owes nothing to
its previous version. Subject: the problem only — the node's own standing lives in
`knowledge/knowledge.status.md`.*

**Where things stand:** the reasoning chain is two steps in. The intent stands as
define (`project.intent.md`); the **system definition landed as capture**
(`system.definition.md`, the landing's why: this log's 0002): the service drawn with
the five-layer model — L1 tolerating retries, concurrency, lost responses, and the
process's own death mid-creation; L2 the sole authority on intent→order, once; L3
undivided on ownership cohesion; L4 a concern map of **five candidate slices**
(duplicate suppression; the concurrent-duplicate race; safe response replay; request
identity; crash during creation) with post-creation recovery, time-based behaviour,
and everything cross-service explicitly deferred; L5 empty by design. The definition's
genre flip to define is judged at the registry — the concern map is the claim the
registry tests.

Position, in the map's names: **`preparation` → `problem framed`** — *project-intent*
and *system-definition* done; standing before **slice-registry born**: the concern map
made actionable, standing per slice, the first slice marked chosen-next (the ordering
decision the definition deliberately does not make), the registry's kind born at that
instance. The thin README is owed at the phase-step's exit, per the adopted projection
cadence.

**Shape** *(snapshot; the repo is the master)*:

```text
internals/
├── project.intent.md        the why — define
├── system.definition.md     the what — capture (flip judged at the registry)
├── log.md                   entries 0001–0002
├── status.md
└── knowledge/               the working node (its own status inside)
```

**Unsettled:**
- **The slice-registry's birth** — the next inner step and the chain's last: the five
  concerns given standing and order, first slice chosen-next with its why in this log;
  concern 4's nature (own slice vs. contract inside another's spec) decided there.
- **The definition's genre flip** — capture → define, judged at the registry's proof
  of the concern map; a logged decision here.
- **The thin README** — owed at *problem framed*'s exit: intent's why (one paragraph),
  the L2 what, the planned-invariants list (from the registry), the method note, a
  status line.

**Next:** the slice-registry's birth.