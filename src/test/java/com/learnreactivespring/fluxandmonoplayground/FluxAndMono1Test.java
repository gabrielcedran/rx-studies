package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMono1Test {
    @Test
    public void fluxTest() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //.concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("After error"))
                .log();

        stringFlux.subscribe(System.out::println,
                (e) -> System.err.println(e),
                () -> System.out.println("Completed"));

    }

    @Test
    public void fluxTestElements() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestElements_withError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class)
                .verify();
    }


    @Test
    public void fluxTestElementsCount_withError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Spring")
                .log();

        StepVerifier.create(stringMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_error() {
        Mono<Object> stringMono = Mono.error(new RuntimeException("Exception occurred."))
                .log();

        StepVerifier.create(stringMono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
