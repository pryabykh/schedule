package com.pryabykh.authserver.userContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
    @Value("${user-id-header-name}")
    private String userIdHeaderName;
    @Value("${user-email-header-name}")
    private String userEmailHeaderName;
    @Value("${correlation-id-header-name}")
    private String correlationIdHeaderName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            String userIdFromHeaders = httpServletRequest.getHeader(userIdHeaderName);
            String userEmailFromHeaders = httpServletRequest.getHeader(userEmailHeaderName);
            String correlationIdFromHeaders = httpServletRequest.getHeader(correlationIdHeaderName);
            UserContextHolder.getContext().setUserId(userIdFromHeaders == null ? null : Long.valueOf(userIdFromHeaders));
            UserContextHolder.getContext().setUserEmail(userEmailFromHeaders);
            UserContextHolder.getContext().setCorrelationId(correlationIdFromHeaders);

            filterChain.doFilter(httpServletRequest, servletResponse);
        } finally {
            UserContextHolder.removeContext();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

}
