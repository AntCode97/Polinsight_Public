package com.dns.polinsight.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public CustomAuthenticationFilter(String defaultFilterProcessUrl) {
    super(defaultFilterProcessUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    // TODO: 2021-08-29 세션 검증 로직
    String username = request.getParameter("email");
    String password = request.getParameter("password");
    return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }

}
