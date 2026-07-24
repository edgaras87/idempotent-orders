# workbench-git.convention.md

> **A workbench convention: how commit messages are built** in the workbench repo and in
> project repos born from it. It is **tree-agnostic**: it never
> defines, lists, or snapshots any repo's structure — the live repo and its node docs carry
> the shape; this convention only defines how commit messages are prepared.
>
> **Scope of the convention: doc work, plus the first code extension.** The types below
> were proven on node/doc work and — so far — carry code commits unchanged. The one
> extension lived need has forced is the **`app` scope** for the system's own source
> (§Scope); further code-commit needs extend this convention when they surface, not
> before.

For a given change: pick a **type**, pick a **scope**, write an **action**, add a **body**
only if the reason is not obvious. Combine them in the format below.

---

## Format

```text
<type>(<scope>): <action>
```

- `type` — the kind of change (§Types). Required.
- `scope` — the node, folder, or root doc changed (§Scope). Required, single token.
- `action` — imperative, specific (§Action). Required.
- body — optional; only when the *why* is not obvious (§Body).

---

## Types

| Type | Use when the change is… | Example |
|---|---|---|
| `capture` | NEW and **provisional** — a vision, draft, rough note, parked idea, or unresolved exploration. | `capture(internals): open node-emigration proposal` |
| `define` | NEW and **trusted** — a finished doc, convention, or concept entry. | `define(workbench): add commit-message convention` |
| `revise` | A CHANGE where the **meaning or decision changes** — re-carve, supersede, correct, re-scope. | `revise(workbench): change model lifecycle framing` |
| `refine` | A CHANGE that only **clarifies or tightens** — decision unchanged. | `refine(workbench): clarify lens-master boundary rule` |
| `decide` | The change **records a resolved choice**, not new structure. | `decide(internals): record entry 0009 — proposal resolved` |
| `map` | Context-state upkeep — status, "you are here", step edits, current-position updates. | `map(workbench): mark project birth current` |

### Picking the type

First match wins:

```text
1. Context-state/status/current-step upkeep?             → map
2. The change records a resolved choice?                 → decide
3. It CHANGES an existing artifact?
     decision/meaning changes                            → revise
     clarity/tightening only, decision unchanged         → refine
4. It is NEW material?
     trusted (finished doc/convention/concept)           → define
     provisional (vision/draft/note/exploration)         → capture
```

Edge rules:

- `define` vs `capture` splits on **trust**, not size. A short settled concept entry can be
  `define`; a long unresolved vision can be `capture`.
- `revise` vs `refine` splits on **decision impact**. If the reader's sense of *what was
  decided* changes, use `revise`. If only the wording improves, use `refine`.
- `decide` is for when *recording the resolved choice* is the point. If structure is produced
  and a decision is merely implied, use `define` or `revise` instead.
- `map` is for state/navigation upkeep, not content meaning.

---

## Scope

The scope is a single lowercase, hyphenated token naming what the change touches.

The scope is **derived from where the changed thing lives**, not chosen from a fixed list. The
repo's live structure carries the scoping. This convention must not maintain a list of current
scopes.

### How to derive a scope token

```text
- Changed thing is the system's own source   → scope `app`.
                                             The system's code, build files, and application
                                             resources (e.g. src/, pom.xml, the build wrapper)
                                             are one subject — the application — wherever the
                                             build tool's layout physically puts them.

- Changed doc lives inside a node/folder     → scope to that node/folder name.
                                             Name the specific file in the action when useful.

- Changed doc lives in the repo root         → scope to that doc filename, without extension.

- Change spans several docs                  → prefer splitting into one logical change per commit.
                                             If it is genuinely one change, scope to the primary
                                             touched node/doc.
```

The location does the work, with one deliberate exception: **`app`** is a *subject* scope,
not a folder name — source layouts are the build tool's shape (`src/main/java/...`), and
folder-derived tokens there name plumbing, not the thing changed; a record doc riding in a
code commit does not change the scope (the code is the primary touched thing). A doc inside
a node/folder reports that node/folder. A loose doc in root reports itself. If a file or
folder is renamed, the derived scope follows the new name; this convention does not need a
scope-list update.

There is no fixed `root` scope. Use the root doc's own filename as the scope, or split the
change if multiple root docs changed for different reasons.

For files using a descriptive source suffix, derive the scope from the document name, not from
the storage suffix. For example, `name.source.md` normally scopes as `name` unless `.source` is
part of the actual document identity.

---

## Action

Write the action in imperative form.

It must be specific and understandable on its own. Avoid generic actions like `update`,
`change`, `fix`, `cleanup`, or `work on`.

When the scope points to a node/folder, the action should name the specific doc or artifact
changed when that is needed for clarity.

```text
good:
  add commit-message convention
  remove stale scope snapshot
  clarify parent-child boundary rule
  record project wrapper as deferred
  mark vision cleanup current

bad:
  update docs
  change stuff
  fix convention
  cleanup
  work on project
```

---

## Body

The body is optional.

Add one only when the *why* is not obvious from the subject line. This is most common for
`revise` and `decide`.

Use the body to explain:

```text
- why the change exists
- what choice was resolved
- what previous framing it replaces
- what tradeoff was accepted
- what old version is superseded
```

The subject line must still stand on its own. The body must not rescue an unclear subject.

---

## Granularity

```text
1 commit = 1 logical change
```

Rules:

- One meaningful change per commit.
- Keep state/map commits separate from content commits when possible.
- Do not split when one small change only makes sense as a single unit.
- If a change touches multiple files only because one decision is being applied consistently,
  it can still be one commit.
- If multiple independent decisions happen together, split them.

---

## How to build a message

```text
1. Pick the type
   Walk the first-match order.

2. Pick the scope
   The system's own source → app.
   Use the node/folder name if the changed doc lives inside one.
   Use the root doc filename if the changed doc lives in the repo root.

3. Write the action
   Imperative, specific, and understandable on its own.

4. Add a body?
   Only if the why is not obvious.

5. Assemble
   <type>(<scope>): <action>
```

---

## One-line rule

```text
Type names the kind of change:
capture = new + provisional,
define = new + trusted,
revise = meaning changed,
refine = wording only,
decide = resolved choice,
map = state/current-position upkeep.

Scope is derived from location:
app for the system's own source,
node/folder name for docs inside a node/folder,
root doc filename for docs in the repo root.
```