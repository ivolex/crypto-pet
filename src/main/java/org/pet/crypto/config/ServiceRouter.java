package org.pet.crypto.config;

import org.pet.crypto.service.ServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.pet.crypto.service.ServiceHandler.DATE;
import static org.pet.crypto.service.ServiceHandler.SYMBOL;

@Configuration
public class ServiceRouter {

    @Autowired
    private ServiceHandler serviceHandler;

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET("api/descendingByRange"), serviceHandler::descendingByRange)
                .andRoute(RequestPredicates.GET("api/values/{" + SYMBOL + "}"), serviceHandler::valuesFor)
                .andRoute(RequestPredicates.GET("api/oldest/{" + SYMBOL + "}"), serviceHandler::oldestOf)
                .andRoute(RequestPredicates.GET("api/newest/{" + SYMBOL + "}"), serviceHandler::newestOf)
                .andRoute(RequestPredicates.GET("api/maxPrice/{" + SYMBOL + "}"), serviceHandler::highestPriceFor)
                .andRoute(RequestPredicates.GET("api/minPrice/{" + SYMBOL + "}"), serviceHandler::lowestPriceFor)
                .andRoute(RequestPredicates.GET("api/minPrice/{" + SYMBOL + "}"), serviceHandler::lowestPriceFor)
                .andRoute(RequestPredicates.GET("api/highestRange/{" + DATE + "}"), serviceHandler::highestRange);
    }
}
