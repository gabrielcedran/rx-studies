package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisher {

    @Test
    public void coldPublisher() throws InterruptedException {
        Flux<String> finiteFlux = Flux.just("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
                .delayElements(Duration.ofSeconds(1))
                .log();

        finiteFlux.subscribe((e) -> System.out.println("Subscriber 1: " + e)); // emits from the beginning

        Thread.sleep(2000);

        finiteFlux.subscribe((e) -> System.out.println("Subscriber 2: " + e)); // emits from the beginning

        Thread.sleep(4000);
    }

    @Test
    public void hotPublisher() throws InterruptedException {
        ConnectableFlux<String> finiteFlux = Flux.just("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
                .delayElements(Duration.ofSeconds(1))
                .log()
                .publish();

        finiteFlux.connect();

        finiteFlux.subscribe((e) -> System.out.println("Subscriber 1: " + e)); // emits from the beginning

        Thread.sleep(4000);

        finiteFlux.subscribe((e) -> System.out.println("Subscriber 2: " + e)); // emits from the current element

        Thread.sleep(4000);
    }
}
