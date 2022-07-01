package com.pryabykh.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;


@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "correlation-id";
    public static final String AUTH_TOKEN = "auth-token";
    public static final String USER_ID = "user-id";
    public static final String USER_EMAIL = "-user-email";

    public String getAuthToken(HttpHeaders requestHeaders) {
        if (requestHeaders.get(AUTH_TOKEN) != null) {
            List<String> header = requestHeaders.get(AUTH_TOKEN);
            return header.stream().findFirst().get();
        } else {
            return null;
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header(name, value)
                                .build())
                .build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}
