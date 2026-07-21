# workbench-roles.model.md

> **A model: what a role is, what a lens is, what a steward is — and how roles relate to
> artifacts.** A model is **knowledge, not law**: each node masters its own rules in its
> own self-knowledge. Living — updated when project work teaches something new.

---

## Role

A **role** is a reusable AI working-context: source docs + an instruction lens, opened again
and again as sessions. A role exists to assist the human on a defined responsibility, under
the human's authority: **the human decides**. How a given node runs its roles — the
confirmation flow, reply shape, session habits — is that node's own law, stated in its
self-knowledge, not here.

## Lens

A role's **lens** (kind `instructions`) is the one artifact class that belongs to a role:
identity, sources with read order, and bootstrap **summaries** of rules — declared
non-authoritative, naming their master (the node's rules-mastering doc): **on any
conflict, the master wins**. The lens is kept **thin**: it carries no truth of its own, and it correctly **dies
with its role**. (A stricter pointers-only lens — no summaries — was tried and reverted: too naked
before its assumption, that every session starts by reading the node's rules master, was
ever tested. Deferred, not discarded.)

A lens typically lives in two places at once — the repo master and a session vehicle's
custom-instructions field — so applying a lens change includes refreshing the vehicle.

## Roles and project truth are different artifact classes

Artifacts produced **while using** a role are **project truth**, never the role's possession:
a correctness spec produced through a reasoning role, code and evidence produced through an
implementation role, infrastructure, public docs — all belong to the project. This is what
lets roles and project truth share one repository without organizing truth by whichever
assistant produced it.

**Written for the need, consumed through lenses:** artifacts are never written *for* a role;
the lens adapts them (which docs, when, how to treat them). Being *about* roles is not being
*written for* one — roles are part of a node's subject, so a node's self-knowledge legitimately describes
them, as truth addressed to any reader. Classification test: ***delete the role — is the
artifact still true and useful?*** The node's self-knowledge, a map, a definition: yes. A
lens: no.

## Steward

The **steward** is a node's special role: it knows the node's artifacts and roles, holds the
full picture, and is the **only writer** — no artifact changes except as the confirmed output
of a steward session. *Only writer* is **authority, not hands**: the physical loop is the
human's (the steward delivers complete files; the human applies, commits, refreshes the
session vehicle's uploads — which can therefore lag; within a session, the newest version
agreed in conversation is current, not the upload). What a steward does around that
authority — proposing before structural moves, keeping the record, the shape of its
replies — is each node's own law, stated in its self-knowledge, not here.

**One steward per node.** Every node has its own; none is shared across any seam — two
nodes always mean two stewards, however closely their subjects relate.

## Narrow roles (hypothesis — deliberately unmodeled)

Practice so far has lived only the steward. The expected pattern — a project steward
delegating to narrow, context-trimmed roles (e.g. a thinker also carrying doc
projection, infrastructure, slice-reasoning, implementation), each doing one thing well,
improved through the steward when they drift — is recorded as a **hypothesis** in `workbench-backend-realization.map.md`, not
here. It becomes a model section only when a project has lived it.

---

## Lifecycle note

The narrow-role hypothesis is the section most likely to grow next — after, not before, a
project exercises it. Corrections arrive as ordinary model updates, decided and logged at
the node that masters this doc — the log, not this note, is the change history.
