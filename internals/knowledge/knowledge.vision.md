# knowledge.vision.md

> The seed doc of this project's **working node**: how the node is handled, and its current
> thinking. The steward reads this and helps build the node by it. *(Born from the
> workbench's knowledge seed; concepts and rationale live in the shipped models — this
> vision masters only this node's own binding rules.)*

## What this node is

This node is the project's **working node** — the apparatus the project was born with,
living whole in **`internals/knowledge/`**, distinct from the project itself: the repo's
name names the project; the project is its code, its guarantees, its surface. The node
exists beside all that to make the work happen. Throughout, **working knowledge** names
the reusable *how* of the work — the node system the apparatus is built on, the
engineering way systems are seen and worked, adopted practice — as opposed to the
problem, the *what worked on*. The node:

- **applies** the working knowledge it carries (`core/` and `realization/` — next section);
- **amends** that knowledge when lived work demands it (the fork rules — **this vision's
  own law**, next section);
- **grows the roles** that assist the work through that knowledge (the steward at birth;
  further roles earned by the work, never ahead of it);
- **produces the project's problem artifacts** through that assistance — definition,
  specs, records, conclusions — each the project's truth, never a role's possession,
  living **outside this node's dir** (see *The two planes*).

Artifacts serve two readers at once: the human and the AI roles assisting them. The node
**describes the whole project — it does not contain it**. The full concept — nodes,
artifacts, kinds, self-knowledge — is mastered as knowledge in
`core/workbench-nodes.model.md`; roles, lenses, and the steward in
`core/workbench-roles.model.md`. Models are knowledge; **this vision is the sole master of
this node's rules**.

## The two planes, and the separation law

The repo's `internals/` splits into two planes:

- **The knowledge plane — this dir, `internals/knowledge/`:** the working node — its four
  docs and the shipped knowledge branches. Everything here is about *how the work is
  organized and assisted*.
- **The problem plane — `internals/` root, outside this dir:** the project's problem
  artifacts — definition, reasoning, specs, conclusions — **plus the problem's own
  record**: a problem `log` and `status`, born by need when problem work starts, run under
  this node's record law (below) but with the **problem as their sole subject**.

**The separation law:** a change on one plane never touches the other in the same act —
work in `internals/` root does not modify `knowledge/`; work in `knowledge/` does not
modify the problem plane. The planes **reference** each other (why, where, what) — they
never edit across. Commits split the same way, scopes derived from location per
`core/workbench-git.convention.md`; one commit never mixes the planes.

## The shipped knowledge — a working fork

The shipped knowledge lives in this node's two branches — mastered at the workbench (the
`workbench-` filename prefix is that provenance), shipped as this project's **working
fork**, not a mirror:

- **`core/`** — the node-system knowledge, general across project types (the two concept
  models, the git convention).
- **`realization/`** — the backend-shaped set (the two engineering models, the realization
  map skeleton, the doc-projection skeleton). A future knowledge type enters as a new
  branch beside these — never mixed in.

The fork rules:

- **Read-first**, but **amendable**: when lived work demands a deviation from a model,
  skeleton, or convention, this project may amend its fork copies — **in place, under the
  same steward discipline as any artifact**. Amendments never rename: the `workbench-`
  prefix is lineage — which upstream master the copy forks — kept through any local
  amendment; divergence is the fork log's story, not the filename's (and a refresh
  matches by name).
- **The fork's own record:** **`fork.log.md`** at this dir's root, born at the first
  deviation, under this node's log law but with a **strictly separate subject**: changes
  to `core/` and `realization/`, and why — never this node's own decisions, never the
  problem. It lives beside the branches, outside them, so a refresh never touches it.
- **Batched flow-back:** learnings return to the workbench batched — at finalization, or
  when something big surfaces mid-project — as `fork.log.md` plus the branches' diffs
  against upstream, handed through the human as ordinary input. This project never edits
  the workbench's masters.
- **Flow-back precedes refresh:** live deviations are flowed back first; a knowledge
  refresh is then a plain replace of **`core/` and `realization/` only** (never this
  node's four docs, never `fork.log.md`) with newer masters, plus a refresh of the session
  vehicle's uploads.

## The project

**Undefined at birth — deliberately.** The project is defined in its own step: the
authoritative statement of what the target system is arrives as ordinary input or is
worked in session, becomes a `definition` artifact on the problem plane (drawn with
`realization/workbench-five-layer-system.model.md`), and is referenced — never restated —
here. Until then, nothing in this node assumes what the system is.

The project follows the workbench's engineering conventions:
`realization/workbench-five-layer-system.model.md` (how a system is *seen*) and
`realization/workbench-correctness-driven-backend.model.md` (how it is *worked*). Its
realization is oriented by the fork's own
`realization/workbench-backend-realization.map.md` directly — the map adopted, molded in
place under the fork rules; execution state lives entirely in the problem plane's record,
coupled to the map by shared step and phase names, never by reference-to-read; a separate
live map artifact is born only by need, per the map's own adoption rule. The project's
doc projection is instantiated from `realization/workbench-doc-projection.skeleton.md`
when the work reaches it.

## Roles, and the steward

One role at birth: the **steward** — sole writer of both planes' artifacts, **in
authority, not hands**: it delivers complete files; the human applies them and commits.
Further roles are earned by the work, never created ahead of it. (What roles, lenses, and
stewards *are*: `core/workbench-roles.model.md` — knowledge, not this node's law.)
**Every rule has exactly one master doc** — this node's rules live in this vision; a lens
carries bootstrap summaries, declared non-authoritative, naming this vision as master: on
any conflict, the vision wins.

**The role split — a hypothesis, not yet law.** Today the steward handles **both
planes**. The expected shape, carried as the narrow-roles hypothesis in
`realization/workbench-backend-realization.map.md`: the steward's native subject is the
**knowledge plane** — the node's docs, the fork, the roles — while producing problem
artifacts is different work with different context; the steward thins into a custodian
(record, orientation, context-preparation) and delegates to a **thinker** role — the
problem's reasoning and its projection: the prepare-thinking-part chain (intent,
definition, registry-birth), the slice-registry (the concern map made actionable — the
execution phase's control point), slice selection, closing slices on evidence, and
internal truth projected to the public surface. That thinker is precisely the separate
role that uses the working knowledge to produce the problem artifacts. The split is
enacted **only by need** — a decision of this node, in its log — and confirmed,
corrected, or discarded by the lived work.

## Naming

Naming follows `core/workbench-naming.convention.md`. This node's own vocabulary under
it: the node's docs carry the subject `knowledge` (`knowledge.vision.md`,
`knowledge.log.md`, `knowledge.status.md`, `knowledge-steward.instructions.md`); the
fork's record is `fork.log.md`; problem-plane artifacts carry **bare subjects**
(`log.md`, `status.md`, later e.g. `system.definition.md`) — the repo name plus
`internals/` scope them; the shipped knowledge files keep their **`workbench-`** prefix,
their provenance. The convention's flattening guard is satisfied here by construction.

## Artifact kinds in use

Born at the first instance, one agreed line each (the mechanism:
`core/workbench-nodes.model.md`). At birth:

- **vision** — direction and current thinking; a living draft.
- **log** — the frozen decision history (handling: *The node's record*, below); also the
  kind of `fork.log.md` and of the problem plane's log, each born at its first need.
- **status** — the snapshot of the present (handling: *The node's record*, below); also
  the kind of the problem plane's status.
- **instructions** — a role's lens: identity, sources, bootstrap summaries.

Expected next (born when their first instance arrives, not before): `definition`, the
slice-registry's kind, the worklog's kind. The shipped knowledge's kind-words (`model`,
`convention`, `skeleton`, `map`) are the **workbench's vocabulary**, carried in the
fork's fixed filenames — not this node's kinds: amending a fork copy is fork work under
the fork rules, never an authored instance; a kind enters this list only when this node
authors its first artifact of it.

## Artifact genres in use

This node's genre vocabulary and law (the axis itself — kind × genre, independent:
`core/workbench-nodes.model.md`); starting rules, this node's own to revise. Two genres:

- **capture** — provisional, still moving; consumed with that caveat.
- **define** — settled, treated as reliable; revised only by decision.

Handling: genre is stated **in the doc's header text, never in the filename** (genre
moves; renames chase references). A **genre flip** — capture → define, or a demotion
back — is a decision, recorded in the log; it never changes the kind. The record docs and
lenses carry no genre — their handling rules already say how they move.

## The node's record

Vision = direction; **log = why**; **status = now** — this node's own division. Three
records live in this repo, one per subject, never mixed:

- **`knowledge.log.md` / `knowledge.status.md`** — this node: its rules, roles, and
  handling decisions.
- **`fork.log.md`** — the fork: changes to `core/` and `realization/` (born at the first
  deviation).
- **The problem plane's `log.md` / `status.md`** — the problem: definition, slices,
  progress (born when problem work starts).

Handling rules, shared by all of them on their own subjects:

- **This vision** — a living draft, rewritten as thinking sharpens: sections are
  replaced, not appended to; it is the **master of this node's rules** (a lens may
  summarize them, naming this vision as master).
- **Logs** — decisions, not touches (test: *would a future reader need to know why this
  happened?*). Only the last entry is **soft** — refinements fold into it; appending the
  next entry **freezes** it; frozen entries are never rewritten — a correction or
  reversal is a new entry referencing the old. Reference, not reading. **Logs are
  per-repo and never inherited.**
- **Statuses** — rewritten **freely, at minimum at session end**; owe nothing to their
  previous versions; the problem status carries a small repo-shape snapshot (the repo
  stays the master of shape). Mid-session rewrites are checkpoints; each costs the human
  an apply-and-commit.

**Session rhythm:** read this vision, then the statuses; record decisions in the right
log while working; end by rewriting the touched statuses.

## How work happens

Sessions under the steward: the human decides; the steward drafts and edits on
confirmation, proposes before structural moves; touched docs delivered complete; replies
short — what changed, what's next, what needs a decision. The physical loop: the steward
delivers complete files; the human applies them, commits (per
`core/workbench-git.convention.md`, the separation law observed), and refreshes the
session vehicle's uploads, which may lag — in session, the newest agreed version is
current, not the upload. Material from earlier work arrives as **ordinary input**.
Nothing is created from a template or ahead of need.

**The change flow:** `comprehend → plan → change`. **Comprehend** comes first and
touches no planning: the idea is reasoned against what exists; its exit is the decision
that a change is warranted — and whether it is one change or several, each planned on
its own. Then:

**The change plan:** when a change is agreed, the steward first delivers a **change
plan** as a document — scaffolding for that one change, never committed (the log records
*why*, the commits record *what changed*; the plan dies when the change lands). Its base
shape: a title (`<change-name> change plan`), what is changing overall, a **contents**
section listing all step titles in order, then the **steps** — each a section with a
title, a description, what it gives us, and its **touched docs**, each doc carrying a
1–5 line note of what changes in it at this step. The plan is shaped **only by the
change's own logic** — no rule limits how many docs a step touches or how many steps
touch one doc. Execution is **per-step**: the human confirms the plan, then each step's
touched docs are delivered complete at that step's state, applied, and committed — **one
step = one commit**, message per the git convention, granularity derived from the plan.
A change small enough to be one step needs no plan document — a plan line in the reply
suffices. Whether problem-plane work runs change plans too, or the method's slice flow
is its own change shape, is an open question this node answers by lived work.

**Amending a plan mid-execution:** the plan has a freeze line — executed steps are
backed by commits, frozen, never rewritten; the remainder is **soft**, freely redrawn
(the plan is uncommitted scaffolding). On a discovery during execution, execution
pauses for a short **eval**, three questions in order: **(1)** does it invalidate
something already committed? → insert a **correction step** — the repo is fixed
forward, history never rewritten; **(2)** does it change the pending steps? → redraw
the soft remainder — refine, insert, reorder, cut; if the change's overall shape
breaks, the plan dies and a new plan is made from the current repo state; **(3)** is it
a different change? → **parked, never absorbed** — it gets its own plan after this one
lands, or this plan is deliberately abandoned if the new change must come first. After
any amendment the steward **redelivers the plan** at its new state, executed steps
marked done; the human confirms before execution resumes — one living scaffold, never
plan versions piling up.

**Delivery form:** touched artifacts are delivered as **documents — streamed artifacts
by default**; file attachments only on the human's request, or where a download
genuinely fits better — never inline in the chat reply. The chat reply itself stays
short — what changed, what's next, what needs a decision — and never restates a
delivered doc's content. Inline display of an artifact happens **only on explicit
request**.

## Open now

- **Prepare the thinking part** — per the fork's map: project-intent, then the
  definition (`definition` kind born at that step), then the slice-registry's birth;
  the problem plane's record born with the first problem decisions.
- Then the working ground, and into execution.
- **Visibility** — this repo starts private; publish only on a real reason.
