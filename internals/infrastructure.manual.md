# infrastructure.manual.md

> **Genre: capture** — written at the ground's setup, verified against a real machine
> as the setup is lived; flips to define by logged decision once the whole ground
> stands. **Audience: the human operator** — how to set up and use everything
> manually. The coder-facing handoff is its own manual (born at this phase-step's
> close). Vocabulary (Execution Environment, Infrastructure Service, Infrastructure
> Service Constraint) is mastered in the coder manual; this doc uses it.

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

### Verify

```sh
podman --version
podman info              # daemonless engine answers; rootless is fine
podman compose config    # parses and echoes compose.yaml — the toolchain proof
```

`config` printing the normalized file (name + services) and exiting clean proves the
file, the front door, and the provider chain — **this is the whole verification while
`services` is empty**: strict providers (docker-compose v2) refuse `up` on a file
with zero services ("no service selected"), and that refusal is correct — there is
nothing to start.

### Operate

From the repo root, once Infrastructure Services are declared:

```sh
podman compose up -d     # bring the declared services up
podman compose ps        # what is running
podman compose down      # stop and remove the containers
```

The compose file is the single declaration of the ground: services are added there
as they are evaluated and constrained, never started ad hoc.