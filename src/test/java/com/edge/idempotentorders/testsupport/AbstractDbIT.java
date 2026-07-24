package com.edge.idempotentorders.testsupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * Spring context + real migrated PostgreSQL, no HTTP server.
 *
 * <p>For database-focused integration tests: migration-pipeline checks, persistence
 * behavior, catalog assertions. HTTP endpoint tests belong on
 * {@link AbstractWebDbIT} instead — each concrete test class extends exactly one
 * base.
 *
 * <p>{@link JdbcClient} matches the application's own data-access choice (plain
 * JDBC, SQL visible — log 0007): tests speak to the database the same way the
 * service does.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class AbstractDbIT extends AbstractPostgresIT {

  @Autowired
  protected JdbcClient jdbc;
}