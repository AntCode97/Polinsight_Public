package com.dns.polinsight.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthProccessingFilter extends AbstractAuthenticationProcessingFilter {

  protected CustomAuthProccessingFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String username = request.getParameter("email");
    String password = request.getParameter("password");
    return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }

}
