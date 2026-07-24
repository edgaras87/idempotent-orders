package com.edge.idempotentorders.probe;

import com.edge.idempotentorders.testsupport.AbstractWebDbIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The bootstrap's exit proof: the harness can <em>create</em> adversity — real
 * concurrency through the full stack (HTTP → app → real PostgreSQL) — not merely
 * call endpoints.
 *
 * <p>Shape: N requests lined up behind a latch and released at the same instant
 * (virtual threads — Java 21), each performing a genuine database round-trip via
 * the probe. Assertion: every one succeeds with the correct body. Deliberately
 * trivial — the probe holds no invariant to violate; what this test proves is the
 * <em>machinery</em> the real slices' evidence will run on. S2's race evidence is
 * this same shape aimed at a real invariant.
 */
class ConcurrentProbeIT extends AbstractWebDbIT {

  @Test
  void concurrentDatabaseRoundTripsAllSucceed() throws Exception {
    int n = 100;
    CountDownLatch ready = new CountDownLatch(n);
    CountDownLatch go = new CountDownLatch(1);

    try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<ResponseEntity<Integer>>> responses = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        responses.add(pool.submit(() -> {
          ready.countDown();
          go.await();                       // line everyone up…
          return http.getForEntity("/probe/db", Integer.class);
        }));
      }
      ready.await();
      go.countDown();                           // …release at one instant

      for (Future<ResponseEntity<Integer>> f : responses) {
        ResponseEntity<Integer> response = f.get();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(1);
      }
    }
  }
}