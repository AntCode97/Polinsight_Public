package com.dns.polinsight.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("rememberMeSuccessHandler")
public class RemeberMeSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    request.getSession().setMaxInactiveInterval(30 * 60);
    response.setStatus(HttpServletResponse.SC_OK);
    response.sendRedirect("/");
  }

}
