package org.pet.crypto.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pet.crypto.model.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = { "prises.path=./src/test/resources/prices" })
class ServiceRouterTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(){
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void routerFunction() {
        webTestClient.get()
                .uri("/api/hello")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response ->
                        assertEquals(response, "Hellllo!!!"));
    }

    @Test
    void returnsValuesForSymbol() {
        webTestClient.get()
                .uri("/api/values/BTC")
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .value(response ->
                        assertEquals(100, response.size()));
    }

    @Test
    void returnsOldest() {
        Crypto expected = new Crypto(1641009600000L, "BTC", 46813.21);
        webTestClient.get()
                .uri("/api/oldest/BTC")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Crypto.class)
                .value(response ->
                        assertEquals(expected, response));
    }

    @Test
    void returnsNewest() {
        Crypto expected = new Crypto(1643659200000L, "BTC", 38415.79);
        webTestClient.get()
                .uri("/api/newest/BTC")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Crypto.class)
                .value(response ->
                        assertEquals(expected, response));
    }

    @Test
    void returnsHighestPrice() {
        Crypto expected = new Crypto(1641081600000L, "BTC", 47722.66);
        webTestClient.get()
                .uri("/api/maxPrice/BTC")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Crypto.class)
                .value(response ->
                        assertEquals(expected, response));
    }

    @Test
    void returnsLowestPrice() {
        Crypto expected = new Crypto(1643022000000L, "BTC", 33276.59);
        webTestClient.get()
                .uri("/api/minPrice/BTC")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Crypto.class)
                .value(response ->
                        assertEquals(expected, response));
    }
}