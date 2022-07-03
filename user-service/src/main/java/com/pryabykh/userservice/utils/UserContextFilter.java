package com.pryabykh.userservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@Component
public class UserContextFilter implements Filter {
    @Value("${user-id-header-name}")
    private String userIdHeaderName;
    @Value("${user-email-header-name}")
    private String userEmailHeaderName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String userIdFromHeaders = httpServletRequest.getHeader(userIdHeaderName);
        String userEmailFromHeaders = httpServletRequest.getHeader(userEmailHeaderName);
        UserContextHolder.getContext().setUserId(userIdFromHeaders == null ? null : Long.valueOf(userIdFromHeaders));
        UserContextHolder.getContext().setUserEmail(userEmailFromHeaders);

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

}
