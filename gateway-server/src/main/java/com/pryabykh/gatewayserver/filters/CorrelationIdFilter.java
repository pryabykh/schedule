package com.pryabykh.gatewayserver.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class CorrelationIdFilter implements GlobalFilter {
    private final FilterUtils filterUtils;
    @Value("${correlation-id-header-name}")
    private String correlationIdHeaderName;

    public CorrelationIdFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        filterUtils.setRequestHeader(exchange, correlationIdHeaderName, generateCorrelationId());
        return chain.filter(exchange);
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
