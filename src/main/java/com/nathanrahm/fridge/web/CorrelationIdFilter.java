package com.nathanrahm.fridge.web;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(1)
public class CorrelationIdFilter implements Filter {
    private static final String X_CORRELATION_ID = "x-correlation-id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            Optional<String> correlationId = Optional.ofNullable(httpServletRequest.getHeader(X_CORRELATION_ID));
            MDC.put(X_CORRELATION_ID, correlationId.orElse(UUID.randomUUID().toString()));

            filterChain.doFilter(httpServletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}