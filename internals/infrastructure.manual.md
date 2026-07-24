# infrastructure.manual.md

> **Genre: capture** — written at the ground's setup, verified against a real machine
> as the setup is lived; flips to define by logged decision once the whole ground
> stands. **Audience: the human operator** — how to set up and use everything
> manually. The coder-facing handoff is its own manual
> (`infrastructure-coder.manual.md`), which also masters the infrastructure
> vocabulary (Execution Environment, Infrastructure Service, Infrastructure Service
> Constraint) this doc uses.

## Execution Environment — podman local containers

The project's Execution Environment is **podman** running local containers, declared
in the repo root's **`compose.yaml`** (`name: idempotent-orders`). All Infrastructure
Services live as services in that file; the environment is fully operable by hand
with the commands below. The canonical command is **`podman compose`** throughout.

### How the pieces relate

Three names, three different things:

- **`podman`** — the container engine itself; runs containers, knows nothing of
  compose files.
- **A compose provider** — the tool that interprets `compose.yaml` into engine
  commands. Any provider works; the two you are likely to meet are **docker-compose
  v2** (the compose reference implementation — despite the name it needs no Docker
  daemon; it talks to podman's socket) and **`podman-compose`** (a standalone python
  tool, with quirks of its own — e.g. it groups a project into a pod).
- **`podman compose`** (with a space) — podman's own front door, and this project's
  canonical command: no compose logic itself, it finds a provider on the machine and
  delegates. It announces the chosen provider with a banner
  (`>>>> Executing external compose provider … <<<<`) — **informational, not an
  error**; silence it via `compose_warning_logs = false` under `[engine]` in
  `~/.config/containers/containers.conf` if it grates.

This manual always uses the front door; whichever provider a machine offers stands
behind it.

### Install

**podman:**

- **Linux:** from the distribution's repositories —
  `sudo dnf install podman` (Fedora) / `sudo apt install podman` (Debian/Ubuntu).
- **macOS / Windows:** install Podman Desktop or the `podman` CLI, then initialize
  and start the machine: `podman machine init && podman machine start`.

**One compose provider** (either satisfies the front door; pick whichever installs
most easily on the machine):

- `podman-compose`: `sudo dnf install podman-compose` / `sudo apt install
  podman-compose`, or `pip install podman-compose`.
- docker-compose v2: `sudo dnf install docker-compose` / `sudo apt install
  docker-compose-v2`, or download the release binary from the compose project on
  GitHub and place it at `~/.docker/cli-plugins/docker-compose` (executable).
- Podman Desktop installs can already carry a provider — run the Verify step first;
  install one only if the front door reports no provider found.

### Verify the environment

```sh
podman --version
podman info              # daemonless engine answers; rootless is fine
podman compose config    # parses and echoes compose.yaml — the toolchain proof
```

`config` printing the normalized file and exiting clean proves the file, the front
door, and the provider chain. Note: `config` also reveals compose's
project-namespacing — the named volume renders as
`idempotent-orders_postgres-data`; that prefix is compose's own, never written in
the file.

## Credentials — `.env`

Secrets and machine-local variance live in **`./.env`** (git-ignored); the committed
template is **`.env.example`**:

```sh
cp .env.example .env     # then edit values if desired — any value works locally
```

Keys: `POSTGRES_BOOTSTRAP_PASSWORD` (the bootstrap identity's password),
`FLYWAY_MIGRATOR_PASSWORD` (**must equal** the migrator password set literally in
`infrastructure/postgres/init/bootstrap.sql`), and optional `POSTGRES_PORT`
(host-side port, default 5432 — set only on conflict). **Decided identities are not
variables**: database, schema, and role names live literal in the files that use
them; changing one is a committed decision, not configuration.

## Infrastructure Service — PostgreSQL

The project's only Infrastructure Service (the evaluation and its constraints:
`internals/log.md` 0006). Runs as the `postgres` service: `postgres:17`, data on the
named volume, host port 5432 (or `POSTGRES_PORT`), healthchecked.

**Reaching it:** from the host (psql, an IDE, a locally run application) the service
is `localhost:5432` — the published port; the service name `postgres:5432` works
only container-to-container on the compose network (which is why Flyway's config
says `postgres` while every command run from the machine says `localhost`).

**Authority split** (the reasoning:
`knowledge/realization/workbench-postgres-role.model.md`): the database itself is
created by the container from `POSTGRES_DB` (compose.yaml), owned by the bootstrap
identity; at the container's **first start**,
`infrastructure/postgres/init/bootstrap.sql` then runs once, connected to that
database, as the bootstrap identity (`postgres`) and creates —

- `idempotent_orders_migrator` — owns schema `idempotent_orders`; the only DDL
  identity; what Flyway connects as;
- `idempotent_orders_runtime` — DML-only via default privileges; what the
  application will connect as;
- the database itself stays owned by the bootstrap identity; database CONNECT is
  revoked from PUBLIC and granted only to the two working identities; the `public`
  schema is stripped of PUBLIC privileges — no application surface.

### Operate

```sh
podman compose up -d     # start (first start runs the bootstrap SQL)
podman compose ps        # expect: idempotent-orders-postgres Up (healthy)
podman compose down      # stop and remove the container (data volume survives)
```

**Full reset** (drops all data; the next `up` re-runs the bootstrap SQL — needed
after any change to `bootstrap.sql`, which only runs against an empty volume):

```sh
podman compose down --volumes
```

### Verify the service and its constraints

Two checks, complementary — the catalog state and the live behavior:

```sh
# 1) the catalog check — roles, ownerships, schema privileges, and the
#    default-privileges mechanism (invisible to \dn+), with expected results
#    commented per section:
podman exec -i idempotent-orders-postgres \
  psql -U postgres -d idempotent_orders \
  < infrastructure/postgres/verify-database-model.sql

# 2) THE behavioral check — runtime attempting DDL must FAIL:
podman exec -it idempotent-orders-postgres \
  psql -U idempotent_orders_runtime -d idempotent_orders -c 'CREATE TABLE t(i int);'
# expected: ERROR: permission denied for schema idempotent_orders
```

The refusal is the ground's governing rule demonstrated live: *the running
application must not control database structure* — enforced by the database's grant
system, not by convention.

## Migrations — Flyway

Flyway is the **only DDL path** (constraint: `internals/log.md` 0006) and connects
as the migrator identity. It is not a running service: a one-shot behind the
`migrate` profile, so plain `up` ignores it. Config:
`infrastructure/flyway/conf/flyway.conf` (no credentials — the compose service
passes them from `.env`); migrations: `infrastructure/flyway/migrations/`
(**empty until a slice earns schema** — the location is provisional and may move to
live with the system's code at *system bootstrapped*; only the compose mount line
would change).

```sh
podman compose run --rm flyway info      # connection + history status, applies nothing
podman compose run --rm flyway migrate   # apply pending migrations (none yet)
podman compose run --rm flyway validate  # applied vs. on-disk consistency
```

`info` against the fresh ground correctly reports: schema empty, history table not
yet created, no migrations found.

## Test runtime — Testcontainers on podman

The evidence harness (`src/test/.../testsupport/`) starts throwaway PostgreSQL
containers through **Testcontainers**, which speaks the Docker API. On this ground
that API is served by **podman's user socket** — one-time setup below. (On a
machine with Docker instead, Testcontainers auto-detects it and none of this is
needed; podman is this project's environment, so podman is what this manual walks.)

### Set up (once per machine)

```sh
# 1) expose podman's Docker-compatible API on the rootless user socket
systemctl --user enable --now podman.socket

# 2) tell Testcontainers where that socket is, and disable Ryuk —
#    Testcontainers' cleanup sidecar, which misbehaves under rootless podman
```

For step 2, prefer the persistent, IDE-friendly form — `~/.testcontainers.properties`:

```properties
docker.host=unix:///run/user/1000/podman/podman.sock
ryuk.disabled=true
```

(replace `1000` with your uid — `id -u`). The environment-variable form works too, but must reach
every JVM that runs tests (shell *and* the IDE's test runner):

```sh
export DOCKER_HOST=unix:///run/user/$(id -u)/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```

### Verify

```sh
systemctl --user status podman.socket          # active (listening)
curl --unix-socket /run/user/$(id -u)/podman/podman.sock \
     http://localhost/_ping                    # → OK
./mvnw test                                    # harness tests pull postgres:17 and run green
```

### Troubleshooting

- **"Could not find a valid Docker environment"** — the socket isn't reachable:
  check the `status` line above, confirm the socket *file* exists at the path, and
  confirm the property/env actually reaches the failing JVM (IDE test runs do not
  inherit shell exports — the properties file avoids the whole class of problem).
- **Socket "active" but the file is missing** — `systemctl --user status
  podman.socket` can report *active (listening)* while the socket file itself is
  gone: `ls -l /run/user/$(id -u)/podman/podman.sock` → *No such file or
  directory*. Active is not enough by itself — the test JVM must reach the actual
  file. Recover by restarting the user units, then confirm the file exists:

  ```sh
  systemctl --user stop podman.socket podman.service
  systemctl --user start podman.socket
  ls -l /run/user/$(id -u)/podman/podman.sock
  ```

- **The properties file seems ignored** — check its location first: it must be
  exactly `~/.testcontainers.properties` (the *home* directory, leading dot). In
  the project root it is read by nobody — lived here as a "Could not find a valid
  Docker environment" failure that vanished the moment the file moved to `$HOME`.
- **Works in terminal, fails in the IDE** — same cause: the IDE's test JVM never
  saw `DOCKER_HOST`. Use `~/.testcontainers.properties`, or set the variables in
  the IDE's run configuration.
- **Leftover test containers** — the accepted cost of disabling Ryuk: a hard-killed
  test JVM can strand its container. `podman ps` to see them,
  `podman rm -f <id>` to clear; they are throwaways, nothing of the ground's data
  lives in them.
- The compose ground and the test runtime are **independent**: `./mvnw test` needs
  no `podman compose up` — the harness brings its own database and migrates it
  itself (the same migration files, applied by the harness instead of the
  one-shot).