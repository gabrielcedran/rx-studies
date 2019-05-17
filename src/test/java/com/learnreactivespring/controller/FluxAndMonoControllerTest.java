package com.learnreactivespring.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void flux_approach_one() {
        Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .expectComplete();
    }

    @Test
    public void flux_approach_two() {
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(4);

    }

    @Test
    public void flux_approach_three() {
        EntityExchangeResult<List<Integer>> result = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .returnResult();

        Assert.assertThat(result.getResponseBody(), Matchers.contains(1, 2, 3, 4));

    }


    @Test
    public void flux_approach_four() {
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .consumeWith(response -> {
                    Assert.assertThat(response.getResponseBody(), Matchers.contains(1, 2, 3, 4));
                });

    }

    @Test
    public void flux_infinitestream_one() {

        Flux<Long> fluxResult = webTestClient.get().uri("/flux/infinite/stream")
            .accept(MediaType.APPLICATION_STREAM_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(Long.class)
            .getResponseBody();

        StepVerifier.create(fluxResult)
            .expectSubscription()
            .expectNext(0L, 1L, 2L)
            .thenCancel()
            .verify();

    }

    @Test
    public void mono_approach_one() {
        webTestClient.get().uri("/mono")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Integer.class)
            .consumeWith(result -> Assert.assertThat(result.getResponseBody(), CoreMatchers.equalTo(1)));
    }

    @Test
    public void mono_approach_two() {
        Flux<Integer> monoResult = webTestClient.get().uri("/mono")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .returnResult(Integer.class)
            .getResponseBody();

        StepVerifier.create(monoResult)
            .expectSubscription()
            .expectNext(1)
            .expectComplete();
    }
}
