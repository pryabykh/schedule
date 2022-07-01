package com.pryabykh.gatewayserver.filters;

import com.pryabykh.gatewayserver.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Order(1)
@Component
public class AuthFilter implements GlobalFilter {
    private final FilterUtils filterUtils;
    private final JwtService jwtService;
    @Value("${gateway.auth.exclude}")
    private List<String> excludedPaths;
    @Value("${user-id-header-name}")
    private String userIdHeaderName;
    @Value("${user-email-header-name}")
    private String userEmailHeaderName;

    public AuthFilter(FilterUtils filterUtils, JwtService jwtService) {
        this.filterUtils = filterUtils;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String currentPath = exchange.getRequest().getPath().toString();
        if (excludedPaths.contains(currentPath)) {
            return chain.filter(exchange);
        }
        String authToken = filterUtils.getAuthToken(exchange.getRequest().getHeaders());
        if (jwtService.isTokenValid(authToken)) {
            filterUtils.setRequestHeader(exchange, userIdHeaderName, jwtService.getUserId(authToken));
            filterUtils.setRequestHeader(exchange, userEmailHeaderName, jwtService.getUserEmail(authToken));
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
