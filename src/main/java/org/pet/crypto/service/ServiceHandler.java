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

        return ServerResponse.ok().body(output, List.class);
    }

    public Mono<ServerResponse> oldestOf(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findOldestFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> newestOf(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findNewestFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> lowestPriceFor(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findMinPriceFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> highestPriceFor(ServerRequest request) {
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findMaxPriceFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> descendingByRange(ServerRequest request) {

        Mono<Map<String, Double>> output = Mono.just(pricesHandler.descendingByRange());

        return ServerResponse.ok().body(output, Map.class);
    }

    public Mono<ServerResponse> highestRange(ServerRequest request) {
        String date = request.pathVariable(DATE);

        Mono<SymbolRange> output;

        SymbolRange range = pricesHandler.highestRangeForDate(date);
        if (Objects.nonNull(range)){
            output = Mono.just(pricesHandler.highestRangeForDate(date));
        }else {
            output = Mono.empty();
        }

        return ServerResponse.ok().body(output, Map.Entry.class);
    }
}
