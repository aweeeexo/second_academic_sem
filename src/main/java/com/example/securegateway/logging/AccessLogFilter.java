package com.example.securegateway.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;

@Component
public class AccessLogFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger("ACCESS");

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    long start = System.currentTimeMillis();
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
    chain.doFilter(requestWrapper, responseWrapper);
    long duration = System.currentTimeMillis() - start;

    String traceId = MDC.get("traceId");
    String method = request.getMethod();
    String path = request.getRequestURI();
    int status = responseWrapper.getStatus();

    log.info("HTTP {} {} -> status={} timeMs={} trace={}", method, path, status, duration, traceId);
    responseWrapper.copyBodyToResponse();
  }
}