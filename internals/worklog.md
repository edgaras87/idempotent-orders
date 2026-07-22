# worklog.md

The problem's happened-record: what was actually done, that the outputs alone don't
show — appended as work happens, entries self-contained, never rewritten; coupled to
the map by shared step and phase names. Kind law: `knowledge/knowledge.vision.md`.
The map yields to this record when reality corrects orientation, never the reverse.

---

**0001 — preparation / infrastructure established: Execution Environment verified —
podman on the ground.** The environment decided in `log.md` 0005 (podman local
containers) verified on the working machine (Fedora 42, rootless): `podman --version`
→ 5.8.2; `podman info` clean — rootless, crun runtime, netavark networking, overlay
on btrfs. Compose tooling surveyed: the machine carries **two compose providers** —
the canonical **`podman compose`** front door delegating to an external provider
(found: docker-compose v2.39.4 at `~/.docker/cli-plugins/docker-compose`, announcing
itself with a provider banner — informational, not an error), and the standalone
python **`podman-compose`** 1.5.0. Canonical command settled: **`podman compose`**
(podman's own front door, provider-agnostic); the python tool remains present but
unused.

**0002 — preparation / infrastructure established: compose ground born and
validated; two provider behaviors met on the way.** `compose.yaml` born at repo root
(`name: idempotent-orders`, `services: {}` — Infrastructure Services join at their
evaluation). The empty ground surfaced a provider-behavior split, lived in this
order: **(a)** `podman-compose up -d` accepted the empty file and created a **pod**
(the printed hash — podman-compose's per-project grouping unit; not a container, so
`ps` correctly listed nothing); downed and cleaned up. **(b)** the canonical
`podman compose up -d` **refused** the empty file — docker-compose v2 exits with
"no service selected": strict, nothing to start. Resolution: with zero services the
correct verification is **validation, not startup** — `podman compose config`
parsed and echoed the normalized file (`name: idempotent-orders`, `services: {}`)
clean. The file, the front door, and the provider chain are proven; `up`/`ps`/`down`
become meaningful at the first real service. No dummy service added to satisfy the
strict provider — the ground stays constrained to need.