package com.pryabykh.authserver.userContext;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class UserContextInterceptor implements RequestInterceptor {
    @Value("${user-id-header-name}")
    private String userIdHeaderName;
    @Value("${user-email-header-name}")
    private String userEmailHeaderName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userIdHeaderLong = UserContextHolder.getContext().getUserId();
        String userIdHeader = userIdHeaderLong != null ? String.valueOf(userIdHeaderLong) : null;
        String userEmailHeader = UserContextHolder.getContext().getUserEmail();
        requestTemplate.header(userIdHeaderName, userIdHeader)
                .header(userEmailHeaderName, userEmailHeader);
    }
}