package org.pet.crypto.service;

import lombok.extern.slf4j.Slf4j;
import org.pet.crypto.model.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class ServiceHandler {

    public static final String SYMBOL = "symbol";

//    @Value("{prises.path:./prices}")
//    private String path;
//
//    private final PricesHandler pricesHandler = new PricesHandler(path);
    @Autowired
    private PricesHandler pricesHandler;

    public Mono<ServerResponse> sayHello(ServerRequest request){
            log.warn("The service component has been called.");
            return ServerResponse.ok().bodyValue("Hellllo!!!");
    }

    public Mono<ServerResponse> valuesFor(ServerRequest request){
        String symbol = request.pathVariable(SYMBOL);

        Mono<List<Crypto>> output = Mono.just(pricesHandler.pricesFor(symbol));

        return ServerResponse.ok().body(output, List.class);
    }

    public Mono<ServerResponse> oldestOf(ServerRequest request){
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findOldestFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> newestOf(ServerRequest request){
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findNewestFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> lowestPriceFor(ServerRequest request){
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findMinPriceFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }

    public Mono<ServerResponse> highestPriceFor(ServerRequest request){
        String symbol = request.pathVariable(SYMBOL);

        Mono<Crypto> output = Mono.just(pricesHandler.findMaxPriceFor(symbol));

        return ServerResponse.ok().body(output, Crypto.class);
    }
}
