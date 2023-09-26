package com.ncsgroup.shipment.server.filter;

import com.ncsgroup.shipment.server.constanst.ProfilingConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
  ) throws ServletException, IOException {
    log.debug(
          "(doFilterInternal)request: {}, response: {}, filterChain: {}",
          request,
          response,
          filterChain
    );

    String accessToken = request.getHeader(ProfilingConstants.AuthConstant.AUTHORIZATION);

    if (Objects.isNull(accessToken)) {
      filterChain.doFilter(request, response);
      return;
    } else if (!accessToken.startsWith(ProfilingConstants.AuthConstant.TYPE_TOKEN)) {
      filterChain.doFilter(request, response);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
