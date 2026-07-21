# workbench-nodes.model.md

> **A model: what a node is, and the mechanics every node shares.** A model is
> **knowledge, not law**: each node masters its own rules in its own self-knowledge; this
> doc explains the concepts that self-knowledge instantiates. Living — updated when
> project work teaches something new.

---

## Node

A **node** is a domain bucket of artifacts for one subject — one project, or one shared
concern. A node owns its artifacts, its rules (mastered in its self-knowledge — see *Self-
knowledge*), its record, and its roles. **One steward per node**; no role is shared across
nodes.

**A node describes its subject — it does not contain it.** The node's boundary is drawn by
**artifact ownership, not directory containment**: its home directory holds exactly its doc
artifacts, while its subject (for a project node: the code, the public surface, the whole
repo) lies largely outside that directory — produced through roles, governed by decisions
the node's record holds, in the full-picture sight of its steward. Nothing escapes the
node's concern by living outside its folder; it just isn't stored as a node artifact.

## Artifact

An **artifact** is a doc — a definition of information, storing knowledge — written for two
readers at once: the **human**, comprehending a complex thing, and an **AI role**, set up to
understand where the human stands so it can assist correctly. Artifacts are **written for
their need, never for a role**; roles reach them through lenses (see
`workbench-roles.model.md`).

**Earned by need:** nothing is created from a template or ahead of demand. A node starts
with its self-knowledge begun; every further artifact exists because the work in front of
us demanded it. Distillation — including this model — happens after the fact, from lived
instances.

Every artifact reads on **two independent axes** — its **kind** and its **genre** (the next
two sections).

## Kind

An artifact's **kind** is what shape of content it is — what a doc *is*: its slot in the
node's vocabulary, carried in the name. **A kind is born at the first instance**: one
agreed line in the node's own law saying what the kind is *for* and how it is handled —
never defined in advance. **Kinds are strictly per-node**: if a kind's handling outgrows
its line, it moves out into an artifact of the same node, referenced from the law — never
into a shared doc; two nodes using the same kind word have two kinds, each mastered in its
own law.

Which kinds exist, and what each is for and how it is handled, is **each node's own
vocabulary — listed and mastered in its own law**, never here. (Illustration only: lived
practice has produced kinds such as instructions, guide, definition, map, skeleton,
convention, model.)

## Genre

An artifact's **genre** is how settled it is — how far a doc can be *trusted*, independent
of what it is. The axes never collapse into each other: a kind names the content's shape; a
genre names its reliability; a genre change never changes the kind.

Which genres exist, what each means, how they are marked, and which kind–genre combinations
are allowed is **each node's own vocabulary and law — listed and mastered in its own law**,
never here; a genre, like a kind, changes only by that node's own decision. (Illustration
only: a capture/define pair, mirroring a git convention's capture/define commit types — the
lived practice the axis was distilled from.)

## Naming

A name is an artifact's **identity across every space it enters**. Artifacts reference
each other by name, and roles consume them through session vehicles that **flatten**
directories away — so a name must stand on its own: unique in every flat space it can
land in, independent of any directory, and stable, because a rename means chasing every
reference. This is why naming is never a per-doc habit: it is **shared practice** a node
adopts as its own law. The lived scheme — `<subject>.<kind>.md`, the prefix and
bare-subject tools, the directory rules — is mastered as a convention,
`workbench-naming.convention.md`: an instance of adopted practice, binding a node only
through its own law.

## Self-knowledge

Every node keeps **self-knowledge**: what it is and where it is heading, its binding rules,
why its decisions were made, where it stands now. Two constitutive rules, and nothing more:

- **One master.** Within a node, its rules have exactly one mastering doc — the node's
  own; every other statement of them (e.g. a lens's bootstrap summaries) is declared
  non-authoritative and names the master — on any conflict, the master wins.
- **Division and handling are the node's own.** How self-knowledge is divided into
  artifacts, which kinds carry it, how its history freezes, how often its snapshot is
  rewritten, what the session rhythm is — all of it is that node's own law, stated in its
  self-knowledge, never here. A model explains what self-knowledge *is*; it dictates
  nothing about how any node runs it.

(Illustration only: the lived division is three-way — direction and rules / decision
history / present snapshot — and the seed ships that division as a born node's starting
rules, its own to revise from day one.)

---

## Lifecycle note

Lived across three nodes and one born project; the naming *rules* moved out to their
own convention (`workbench-naming.convention.md`) — adopted practice; the model keeps
only why naming is shared. Corrections
arrive as ordinary model updates, decided and logged at the node that masters this doc —
the log, not this note, is the change history.
