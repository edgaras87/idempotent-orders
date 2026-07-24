package com.edge.idempotentorders.db;

import com.edge.idempotentorders.testsupport.AbstractDbIT;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves the harness's migration loop end to end: container up → harness-side
 * Flyway ran against it → Spring context booted → the database is queryable.
 *
 * <p>The assertion is deliberately about the <em>pipeline</em>, not schema: the
 * {@code flyway_schema_history} table existing (the query not failing) proves
 * migrate ran; zero applied rows is the correct state — the migrations home is
 * deliberately empty until a slice earns schema (log 0007). The first real
 * migration will turn this count positive without changing what the test proves.
 */
class MigrationPipelineIT extends AbstractDbIT {

  @Test
  void flywayHistoryTableExistsWithNoAppliedMigrations() {
    Integer applied = jdbc.sql("select count(*) from flyway_schema_history")
      .query(Integer.class)
      .single();

    assertThat(applied).isZero();
  }
}