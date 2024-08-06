package org.pet.crypto.service;

import lombok.extern.slf4j.Slf4j;
import org.pet.crypto.model.Crypto;
import org.pet.crypto.model.SymbolRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@Slf4j
public class ServiceHandler {

    public static final String SYMBOL = "symbol";
    public static final String DATE = "date";

    @Autowired
    private PricesHandler pricesHandler;

    public Mono<ServerResponse> valuesFor(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<List<Crypto>> output = Mono.just(pricesHandler.pricesFor(symbol));

        return output
                .flatMap(list -> ok().contentType(APPLICATION_JSON).bodyValue(list))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> oldestOf(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findOldestFor(symbol));

        return output
                .flatMap(crypto -> ok().contentType(APPLICATION_JSON).bodyValue(crypto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> newestOf(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findNewestFor(symbol));

        return output
                .flatMap(crypto -> ok().contentType(APPLICATION_JSON).bodyValue(crypto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> lowestPriceFor(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findMinPriceFor(symbol));

        return output
                .flatMap(crypto -> ok().contentType(APPLICATION_JSON).bodyValue(crypto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> highestPriceFor(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findMaxPriceFor(symbol));

        return output
                .flatMap(crypto -> ok().contentType(APPLICATION_JSON).bodyValue(crypto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> descendingByRange(ServerRequest request) {

        Mono<Map<String, Double>> output = Mono.justOrEmpty(pricesHandler.descendingByRange());

        return output
                .flatMap(map -> ok().contentType(APPLICATION_JSON).bodyValue(map))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> highestRange(ServerRequest request) {
        String date = request.pathVariable(DATE);

        Mono<SymbolRange> output = Mono.justOrEmpty(pricesHandler.highestRangeForDate(date));

        return output
                .flatMap(symbol -> ok().contentType(APPLICATION_JSON).bodyValue(symbol))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
