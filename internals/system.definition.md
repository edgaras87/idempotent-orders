# system.definition.md

> **Genre: define.** The authoritative statement of what the system is, drawn with
> `workbench-five-layer-system.model.md` — settled; revised only by decision, recorded
> in the problem log (flipped from capture at the registry's birth: `log.md` 0004).
> Downstream artifacts reference this doc, never restate it.

---

## What this service is — the motivating guarantee

The service exists to make order *creation* correct under an unreliable caller and an
unreliable network. Stated as the guarantee it sells at its edge: *give me the same
purchase intent a hundred times, concurrently or spread over hours, and there will be
one order and one order only — and every one of those hundred callers gets an answer
consistent with that single order.* What the service owns, bounds, and decides is L2's
statement below — this section only says why it is worth building.

---

## The layers (this service as system of interest)

Drawn top-down by the five-layer model, scoped to *this* service (the model's
scoping-is-relative rule — inside a larger system this whole service collapses to one
L3 area of a wider L2; that wider analysis is project 2's, not this one's).

### L1 — Environment (what it must tolerate, not control)

- **Callers**: real external clients (an API gateway, a checkout frontend, another
  service) that **retry** — because they time out, lose the response, or fire
  duplicates — and that may send the same intent **concurrently**. Their retry
  behaviour is a fact to absorb, not something the service can forbid.
- **The network**: at-least-once in effect — responses get lost, requests arrive more
  than once, and two copies of one intent can land at the same instant.
- **The process itself can die mid-work**: crashes and restarts are a fact of the
  runtime, not an exception to design away — the service may fail *after* persisting
  an order but *before* answering, and the caller's timeout-and-retry then meets a
  state the naive design never considered. Absorbed into the adversity model, scoped
  to creation (see L4, concern 5).
- **The datastore**: assumed to offer atomic primitives (transactions, unique
  constraints); the service relies on these as its last line of defence but does not
  own them.

### L2 — System of Interest (responsibility + boundary + authority)

- **Owns**: the mapping *purchase intent → at most one order*; the order-identity
  decision; the response contract that lets a caller safely retry.
- **Boundary — inside**: accepting a create-order request, deciding new-vs-duplicate,
  persisting the one order, returning a result a retry can rely on.
- **Boundary — outside**: payment, inventory, fulfilment, order lifecycle after
  creation, the caller's own retry policy. Absorbed as environment, never owned.
- **Authority**: within its boundary the service is the **sole authority** on whether
  an order exists for an intent. No external signal overrides that.

### L3 — Responsibility Areas (internal ownership)

**Provisionally none.** One owner of state and decisions — ownership is cohesive:
every concern below acts on the same state (the intent→order mapping) under the same
authority, so no internal boundary earns its keep. Decompose only if cohesion breaks
in lived work (a plausible seam: request-intake/deduplication vs. order-persistence —
noted, not drawn).

### L4 — Local Problems (the concern map — the slice surface)

The bounded problems this service must solve; each is a candidate **slice** (one
invariant–adversity pairing carried to evidence). Named plainly, summary-level, not
yet worked — ordering and standing are the registry's decisions, not this doc's:

1. **Duplicate suppression** *(dominant)* — invariant: N identical requests → exactly
   one persisted order. Adversity: **duplicate delivery** (retries). The service's
   reason to exist.
2. **The concurrent-duplicate race** — the dominant invariant *under* **contention**:
   two copies of one intent racing at the same instant, where naive check-then-insert
   admits two orders. This is where the depth lives — duplicate suppression and
   contention co-occurring, not as separate concerns but as the hard case of the same
   guarantee.
3. **Safe response replay** — invariant: every retry of an already-created intent gets
   an answer consistent with the single order (not a spurious error, not a second
   creation). Adversity: duplicate delivery again, seen from the response side.
4. **Request identity** — what makes two requests "the same intent": the idempotency
   key contract (client-supplied? derived?), its validation, and its collision
   behaviour. Upstream of 1–3, since the others are defined against it. A
   slice-candidate like the rest — whether it is a slice of its own or a contract
   worked inside another slice's spec is the registry's call.
5. **Crash during creation** — invariant: a process death at any point of the creation
   path leaves the world in a state the retry converges to one order from — either no
   order (the retry creates it) or the one order (the retry replays its answer);
   never a half-order, never a second. Adversity: **partial failure**, scoped strictly
   to the creation path.

*Deferred by design* (not this project): expiration and time-based behaviour; anything
cross-service; and **post-creation recovery** — crashes, transitions, and convergence
*after* the order exists are lifecycle territory, a later project's subject. If a
concern here turns out to need another area or service to be correct, it belongs to a
later project, not here.

### L5 — Interaction / Composition

**Empty by design.** This service has no internal areas to compose and does not
coordinate with siblings — that is precisely what makes it a single-area service.