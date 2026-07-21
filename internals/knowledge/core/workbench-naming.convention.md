# workbench-naming.convention.md

> **A workbench convention: how artifacts and directories are named** in the workbench
> repo and in project repos born from it. Genre: **define** — settled, lived practice,
> revised by decision. A convention is adopted practice, never any node's rules master: it
> binds a node only through that node's own law.

## The scheme

Every artifact is **`<subject>.<kind>.md`**. The kind lives **only in the dotted slot** —
the last dot-separated token before `.md`; it is drawn from the owning node's kind list,
and there is exactly one. Hyphens are ordinary subject characters: a kind-word appearing
hyphenated inside a subject carries **no** kind semantics — only the dotted slot does.
This keeps kinds machine-distinguishable (`*.model.md` lists every model) and subjects
free to use any words.

**Genre never enters the name** — genre changes over time, and a rename means chasing
every reference. Genre is stated in the doc's header text.

## The flattening guard — the underlying rule

Session vehicles (browser Projects) drop directories, so **a name must be unique in every
flat space it enters** — and renaming later means chasing every reference. Everything
below is a tool for satisfying this one rule:

- **The node-name prefix** is the default tool — a node's own docs `<node>.<kind>.md`,
  its inner things `<node>-<thing>.<kind>.md` — required wherever several nodes' docs can
  share a flat space.
- **Bare subjects** are allowed where uniqueness holds by construction — e.g. a repo
  whose flat spaces hold one node's docs plus prefixed foreign-mastered files.
- **A bare-named doc leaving its repo** as ordinary input elsewhere gets a distinguishing
  name at import.

## Directories

Directories may carry meaning inside a repo (a node's dir, a marked destination dir), but
**no name may depend on its directory** — the flat spaces erase them.

**No directory in a node's repo is ever named after another node** — a dir holding
another node's material is named for its role in the repo it lives in, while its *files*
may carry their origin's prefix as provenance. A folder bearing another node's name
manufactures a phantom node the moment that node is mentioned in text.
