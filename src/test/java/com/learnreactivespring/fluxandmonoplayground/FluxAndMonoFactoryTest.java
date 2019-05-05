package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "ana", "jack", "jenny");

    @Test
    public void fluxUsingIterable() {

        Flux<String> namesFlux = Flux.fromIterable(names)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam", "ana", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        Flux<String> namesFlux = Flux.fromArray(names.toArray(names.toArray(new String[]{})));

        StepVerifier.create(namesFlux)
                .expectNext("adam", "ana", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(names.stream());

        StepVerifier.create(namesFlux)
                .expectNext("adam", "ana", "jack", "jenny")
                .verifyComplete();

    }

    @Test
    public void fluxUsingRange() {
        Flux<Integer> integerFlux = Flux.range(1, 5);

        StepVerifier.create(integerFlux.log()).expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    public void fluxUsingDifferentTypes() {
        Flux<Object> objectFlux = Flux.just("A", 5, true);

        StepVerifier.create(objectFlux.log()).expectNext("A", 5, true)
                .verifyComplete();
    }
}
