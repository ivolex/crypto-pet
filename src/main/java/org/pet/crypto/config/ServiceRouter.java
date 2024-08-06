package org.pet.crypto.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.pet.crypto.model.Crypto;
import org.pet.crypto.model.SymbolRange;
import org.pet.crypto.service.ServiceHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;
import java.util.Map;

import static org.pet.crypto.service.ServiceHandler.DATE;
import static org.pet.crypto.service.ServiceHandler.SYMBOL;

@Configuration
public class ServiceRouter {

    private static final String DESCENDING_PATH = "api/descendingByRange";
    private static final String VALUES_PATH = "api/values/{" + SYMBOL + "}";
    private static final String OLDEST_PATH = "api/oldest/{" + SYMBOL + "}";
    private static final String NEWEST_PATH = "api/newest/{" + SYMBOL + "}";
    private static final String HIGHEST_PRICE_PATH = "api/maxPrice/{" + SYMBOL + "}";
    private static final String LOWEST_PRICE_PATH = "api/minPrice/{" + SYMBOL + "}";
    private static final String HIGHEST_RANGE_PATH = "api/highestRange/{" + DATE + "}";

    @Autowired
    private ServiceHandler serviceHandler;

    @Bean
    @RouterOperations(
            {
                    @RouterOperation(path = DESCENDING_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "descendingByRange",
                            operation = @Operation(operationId = "descendingByRange", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Map.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                            )),

                    @RouterOperation(path = VALUES_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "valuesFor",
                            operation = @Operation(operationId = "valuesFor", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = List.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                                    , parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = SYMBOL)}
                            )),

                    @RouterOperation(path = OLDEST_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "oldestOf",
                            operation = @Operation(operationId = "oldestOf", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Crypto.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                                    , parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = SYMBOL)}
                            )),

                    @RouterOperation(path = NEWEST_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "newestOf",
                            operation = @Operation(operationId = "newestOf", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Crypto.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                                    , parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = SYMBOL)}
                            )),

                    @RouterOperation(path = HIGHEST_PRICE_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "highestPriceFor",
                            operation = @Operation(operationId = "highestPriceFor", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Crypto.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                                    , parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = SYMBOL)}
                            )),

                    @RouterOperation(path = LOWEST_PRICE_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "lowestPriceFor",
                            operation = @Operation(operationId = "lowestPriceFor", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Crypto.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                                    , parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = SYMBOL)}
                            )),

                    @RouterOperation(path = HIGHEST_RANGE_PATH, produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = ServiceHandler.class, beanMethod = "highestRange",
                            operation = @Operation(operationId = "highestRange", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SymbolRange.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")}
                                    , parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = DATE)}
                            )),

            })
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET(DESCENDING_PATH), serviceHandler::descendingByRange)
                .andRoute(RequestPredicates.GET(VALUES_PATH), serviceHandler::valuesFor)
                .andRoute(RequestPredicates.GET(OLDEST_PATH), serviceHandler::oldestOf)
                .andRoute(RequestPredicates.GET(NEWEST_PATH), serviceHandler::newestOf)
                .andRoute(RequestPredicates.GET(HIGHEST_PRICE_PATH), serviceHandler::highestPriceFor)
                .andRoute(RequestPredicates.GET(LOWEST_PRICE_PATH), serviceHandler::lowestPriceFor)
                .andRoute(RequestPredicates.GET(HIGHEST_RANGE_PATH), serviceHandler::highestRange);
    }
}
