package com.edge.idempotentorders.testsupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * The full stack under test: random-port HTTP server + real migrated PostgreSQL.
 *
 * <p>This is the evidence tier — where adversity is generated through the same door
 * a real caller uses (HTTP → app → PostgreSQL), so what survives here is what the
 * service actually guarantees at its boundary. Database-only tests belong on
 * {@link AbstractDbIT}; each concrete test class extends exactly one base.
 *
 * <p>{@code TestRestTemplate} imports are the Boot 4 packages
 * ({@code org.springframework.boot.resttestclient}).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public abstract class AbstractWebDbIT extends AbstractPostgresIT {

  @Autowired
  protected JdbcClient jdbc;

  @Autowired
  protected TestRestTemplate http;
}