package com.vezbe.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String method = String.valueOf(exchange.getRequest().getMethod());
        String path = exchange.getRequest().getURI().getPath();

        System.out.println("GATEWAY REQUEST: " + method + " " + path);

        return chain.filter(exchange)
                .doFinally(signalType -> {
                    System.out.println(
                            "GATEWAY RESPONSE: "
                                    + exchange.getResponse().getStatusCode()
                                    + " | "
                                    + method
                                    + " "
                                    + path
                    );
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
