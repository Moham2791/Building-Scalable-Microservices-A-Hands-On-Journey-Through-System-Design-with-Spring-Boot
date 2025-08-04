package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        // System Design: Load Balancing and Routing
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .uri("lb://user-service"))
                .route("task-service", r -> r.path("/tasks/**")
                        .uri("lb://task-service"))
                .build();
    }
}