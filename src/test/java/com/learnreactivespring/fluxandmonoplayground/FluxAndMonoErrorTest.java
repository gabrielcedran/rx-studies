package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error test")))
                .concatWith(Flux.just("D", "E"))
                .onErrorResume((e) -> {
                    System.out.println("Exception is: " + e);
                    return Flux.just("Default value", "default 2");
                });


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "Default value", "default 2")
                //.expectError(RuntimeException.class)
                .verifyComplete();


    }

    @Test
    public void fluxErrorHandling_onErrorReturn() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error test")))
                .concatWith(Flux.just("D", "E"))
                .onErrorReturn("default");


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "default")
                //.expectError(RuntimeException.class)
                .verifyComplete();


    }

    @Test
    public void fluxErrorHandling_onErrorMap() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error test")))
                .concatWith(Flux.just("D", "E"))
                .onErrorMap(e -> new ClassNotFoundException());


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(ClassNotFoundException.class)
                .verify();


    }

    @Test
    public void fluxErrorHandling_retry() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error test")))
                .concatWith(Flux.just("D", "E"))
                .retry(1);


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C","A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();


    }

    @Test
    public void fluxErrorHandling_retryBackOff() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error test")))
                .concatWith(Flux.just("D", "E"))
                .retryBackoff(1, Duration.ofSeconds(2));


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C","A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();


    }
}
