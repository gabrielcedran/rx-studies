package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMono3TransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {

        Flux<String> stringFlux = Flux
                .fromIterable(names)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux).expectNext("ADAM", "ANNA", "JACK", "JENNY").verifyComplete();

    }

    @Test
    public void transformUsingMap_toInteger() {

        Flux<Integer> stringFlux = Flux.fromIterable(names).map(String::length).log();

        StepVerifier.create(stringFlux).expectNext(4, 4, 4, 5).verifyComplete();

    }

    @Test
    public void transformUsingMap_toInteger_repeat() {

        Flux<Integer> stringFlux = Flux.fromIterable(names)
                .filter(n -> n.length() > 4)
                .map(String::length)
                .repeat(1).log();

        StepVerifier.create(stringFlux).expectNext(5, 5).verifyComplete();

    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap(s -> Flux.fromIterable(convertToList(s)))
                //.flatMap(s -> s)
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap_parallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(3)
                .map(s -> s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(s -> s)
                //.flatMap(s -> s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                //.flatMap(s -> s)
                //.map(this::convertToList)
                //.flatMap(s -> Flux.fromIterable(s))
                .map(s -> Flux.fromIterable(s))
                .flatMap(s -> s)
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap_parallel_ordered() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(3)
                .flatMapSequential(e -> e.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(e -> Flux.fromIterable(e))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "new value");
    }
}
