# workbench-postgres-role.model.md

> **A workbench model: how database authority is split into roles on PostgreSQL.**
> **Provisional — and not yet workbench-mastered:** authored *for* the workbench inside
> its first using project, from prior-work input; it reaches the workbench through that
> project's flow-back, with this first lived application as its evidence (standing and
> record: that project's `fork.log.md`). Generic PostgreSQL throughout — a using
> project's concrete names, passwords, and wiring live in that project's own manuals,
> never here.

---

## The principle

**Database availability → database authority control.** Making a database available to
a system is not one grant but two decisions: *who may change what the database is*
(structure — DDL), and *who may change what it holds* (data — DML). The rule this
model enforces:

> **The running application must not control database structure.**

Schema exists solely through versioned migrations; the application operates on data
inside a structure it cannot alter. The split is enforced by the **database's own
grant system** — never by convention, code review, or trust: an application identity
that *cannot* issue DDL makes structural drift impossible rather than forbidden.

## The roles

Three identities, two of them the model's own:

- **`migrator`** — the structure authority. **Owns the application schema** and every
  object in it; the **only identity migrations run as** (Flyway or equivalent
  connects as `migrator`, nothing else does). Holds DDL within that schema — and
  only there: it does **not** own the database, cannot drop it, and creates no
  schemas beyond its own. Not a superuser.
- **`runtime`** — the data authority; **what the running application connects as.**
  DML only: read and write rows, use sequences — no create, no alter, no drop, no
  ownership. If the application attempts DDL, the database refuses.
- *(the bootstrap identity)* — the server's existing admin role (`postgres` in a
  stock container; **never created by this model — it is already there**) runs the
  setup **once**: creates the database (and owns it), creates the two roles and the
  application schema, sets the grants. It is not part of the running system and
  never appears in application or migration configuration.

**Database ownership stays above the split** — with the bootstrap identity by
default. A dedicated database-owner role between bootstrap and `migrator` is a real
tier on shared or managed clusters (where the server's admin is not yours to use);
it is an **extension by need**, not the base model — locally it would be a fourth
identity that acts exactly once.

## The application schema — own it, don't use `public`

The model's mechanism is **schema-scoped ownership**: `migrator` owns the application
schema, and everything below hangs off that ownership. The default `public` schema is
the wrong home — it is a shared, legacy surface (owned by `pg_database_owner`, its
default privileges a moving target across Postgres versions), and ownership of it is
not the migrator's to have. A **project-named schema** makes the authority boundary a
created, owned, explicit thing; the application's `search_path` names it.

## The grant boundaries

The shape, as SQL — run once by the bootstrap identity (names illustrative):

```sql
-- as the bootstrap identity (e.g. postgres), once
CREATE ROLE migrator LOGIN;
CREATE ROLE runtime  LOGIN;
CREATE DATABASE app;                    -- owned by the bootstrap identity

-- connected to app, as the bootstrap identity
CREATE SCHEMA app AUTHORIZATION migrator;
GRANT USAGE ON SCHEMA app TO runtime;

-- future objects migrator creates arrive consumable by runtime automatically
ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA app
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE migrator IN SCHEMA app
  GRANT USAGE, SELECT ON SEQUENCES TO runtime;
```

The load-bearing line is the **default privileges** pair: every table and sequence a
future migration creates arrives already granted to `runtime` — the split needs no
per-migration grant discipline, so it cannot erode migration by migration.

`runtime` deliberately receives no `TRUNCATE`, no `REFERENCES`, no `CREATE` anywhere;
`migrator` deliberately owns one schema, not the database and not the server.

## Naming across a cluster

Roles are **cluster-wide** — shared by every database on the instance. On any cluster
serving more than one project, role names carry the project as a prefix
(`<project>_migrator`, `<project>_runtime`), underscored — hyphens are not legal in
unquoted SQL identifiers, and quoted names are a permanent tax. The schema, living
inside the project's database, may carry the bare project name.

## Why this split and not less

- **One identity (app == owner):** the common default, and the principle's direct
  violation — any ORM auto-DDL, any stray migration call, any injected statement can
  restructure the database. Refused.
- **Convention only ("the app just doesn't run DDL"):** unenforced rules decay; the
  grant system is the mechanism that makes the rule a property.
- **More roles (a dedicated database owner, readers, per-service identities,
  row-level security):** real needs at larger scope — extensions by need, not the
  base model. This model is the minimal authority split; a project adds roles as its
  own decisions.

## What a using project supplies

The model is generic; a project wires it: concrete role names under the cluster
naming rule and their credentials (secrets handling is the project's), the schema
name and the application's `search_path`, which migration tool connects as
`migrator` (Flyway in the first using project), and where the bootstrap SQL lives
and how it is applied — all of that belongs in the project's manuals, referencing
this model, never restating it.

---

## Lifecycle note

Born inside its first using project from prior-work input; corrected in the same
session before first use (database ownership moved out of `migrator`; the
application-schema rule stated; `runtime` as the data identity's name; the cluster
naming seam added). First lived application: that project's PostgreSQL setup (its
worklog is the happened-record). Owed with the project's flow-back batch:
confirmation or correction from the lived wiring, then workbench mastery.
Corrections arrive as ordinary model updates, logged at the node that masters this
doc.