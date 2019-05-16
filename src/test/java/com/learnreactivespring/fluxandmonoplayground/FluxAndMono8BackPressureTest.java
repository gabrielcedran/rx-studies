package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMono8BackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenRequest(3)
                .expectNext(3,4,5)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        finiteFlux.subscribe((element) -> System.out.println("Element: " + element),
                (err) -> System.err.println(err),
                () -> System.out.println("Completed!!!"),
                subscription -> subscription.request(3));
    }

    @Test
    public void backPressure_cancel() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        finiteFlux.subscribe((element) -> System.out.println("Element: " + element),
                (err) -> System.err.println(err),
                () -> System.out.println("Completed!!!"),
                subscription -> subscription.cancel());
    }

    @Test
    public void customized_backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();

        finiteFlux.subscribe(new BaseSubscriber<Integer>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {

                System.out.println("Value: " + value);
                if (value < 4) {
                    request(2);
                    //cancel();
                }
            }
        });
    }
}
