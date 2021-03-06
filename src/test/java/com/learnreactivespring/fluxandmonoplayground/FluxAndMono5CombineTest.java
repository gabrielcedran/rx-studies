package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMono5CombineTest {

    @Test
    public void combineUsingMerge() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.merge(flux1, flux2).log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingMerge_delay() {

        Flux<String> flux1 = Flux.just("A", "B", "C").filter(e -> e.equals("A")).delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(flux1, flux2).log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat() {

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(flux1, flux2).log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void combineUsingZip() {

        Flux<String> flux1 = Flux.just("A", "B", "C", "A1", "A2");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.zip(flux1, flux2, String::concat).log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();

    }
}
