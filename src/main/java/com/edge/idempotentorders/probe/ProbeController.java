package com.edge.idempotentorders.probe;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCAFFOLDING — dies when the first real slice lands.
 *
 * <p>The bootstrap's probe: one endpoint performing a real database round-trip
 * (`SELECT 1` as the runtime identity), existing only so the evidence harness has a
 * full HTTP → app → PostgreSQL path to generate adversity through before any real
 * behavior exists. It carries no business meaning and must not grow any: the
 * create-order endpoint is S1's work, born from its correctness spec, not from
 * this.
 */
@RestController
class ProbeController {

  private final JdbcClient jdbc;

  ProbeController(JdbcClient jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping("/probe/db")
  int probeDb() {
    return jdbc.sql("select 1").query(Integer.class).single();
  }
}