package com.miguel.webflux.config;

import com.miguel.webflux.handler.DishHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    //Functional Endpoints
    @Bean
    public RouterFunction<ServerResponse> routes(DishHandler handler) {
        return route(GET("/v2/dishes"), handler::findAll)
                .andRoute(GET("/v2/dishes/{id}"), handler::findById)
                .andRoute(POST("/v2/dishes"), handler::create)
                .andRoute(PUT("/v2/dishes/{id}"), handler::update)
                .andRoute(DELETE("/v2/dishes/{id}"), handler::delete);
    }
}
