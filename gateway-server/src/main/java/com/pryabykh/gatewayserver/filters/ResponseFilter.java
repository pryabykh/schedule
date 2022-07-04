package com.pryabykh.gatewayserver.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {
	private FilterUtils filterUtils;
    @Value("${correlation-id-header-name}")
    private String correlationIdHeaderName;

    public ResponseFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            	  HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            	  String correlationId = filterUtils.getCorrelationId(requestHeaders);
                  exchange.getResponse().getHeaders().add(correlationIdHeaderName, correlationId);
              }));
        };
    }
}