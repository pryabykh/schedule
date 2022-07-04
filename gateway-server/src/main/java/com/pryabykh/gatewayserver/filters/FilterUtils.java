package com.pryabykh.gatewayserver.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;


@Component
public class FilterUtils {
    @Value("${auth-token-header-name}")
    private String userEmailHeaderName;
    @Value("${correlation-id-header-name}")
    private String correlationIdHeaderName;

    public String getAuthToken(HttpHeaders requestHeaders) {
        if (requestHeaders.get(userEmailHeaderName) != null) {
            List<String> header = requestHeaders.get(userEmailHeaderName);
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

    public String getCorrelationId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(correlationIdHeaderName) != null) {
            List<String> header = requestHeaders.get(correlationIdHeaderName);
            return header.stream().findFirst().get();
        } else {
            return null;
        }
    }

}
