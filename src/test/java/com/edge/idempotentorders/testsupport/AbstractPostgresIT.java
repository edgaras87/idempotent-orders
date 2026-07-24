package com.edge.idempotentorders.testsupport;

import org.flywaydb.core.Flyway;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * The harness's ground: one throwaway PostgreSQL per test JVM, migrated before any
 * Spring context boots.
 *
 * <p>Singleton pattern, deliberately: the container is started once in the static
 * initializer and shared by every integration test in the run — fast, and its
 * lifecycle is ours (no JUnit-managed per-class restarts). {@code @ServiceConnection}
 * lets Spring wire the test datasource straight from this field, so no connection
 * details are duplicated anywhere in test configuration.
 *
 * <p>The migrate call below is the harness playing the role the compose one-shot
 * plays against the real ground: <em>something outside the app migrates; the app
 * runs.</em> It applies the one migrations home — {@code
 * infrastructure/flyway/migrations}, read from the filesystem, no classpath copy to
 * drift — and the app under test then boots in exactly its shipped posture: no
 * migration machinery, structure assumed to exist.
 *
 * <p>Image pinned to postgres:17 — the same major as the compose ground, so tests
 * exercise the database the service actually runs on.
 */
public abstract class AbstractPostgresIT {

  @ServiceConnection
  static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

  static {
    postgres.start();
    Flyway.configure()
      .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
      .locations("filesystem:infrastructure/flyway/migrations")
      .load()
      .migrate();
  }
}