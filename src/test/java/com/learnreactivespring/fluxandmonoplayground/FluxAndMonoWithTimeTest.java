package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {

        Flux<Long> infiteFlux = Flux.interval(Duration.ofMillis(200)).log();

        infiteFlux.subscribe(e -> System.out.println("the value is " + e));

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequence_test() {

        Flux<Long> infiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(3).log();

        StepVerifier.create(infiteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();

    }

    @Test
    public void infiniteSequenceMap_test() {

        Flux<String> infiteFlux = Flux.interval(Duration.ofMillis(200))
                .map(e -> e.toString()).take(3).log();

        StepVerifier.create(infiteFlux)
                .expectSubscription()
                .expectNext("0", "1", "2")
                .verifyComplete();

    }

    @Test
    public void infiniteSequenceMap_withDelay_test() {

        Flux<String> infiteFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map(e -> e.toString()).take(3).log();

        StepVerifier.create(infiteFlux)
                .expectSubscription()
                .expectNext("0", "1", "2")
                .verifyComplete();

    }
}
