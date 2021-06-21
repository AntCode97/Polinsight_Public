package com.dns.polinsight.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    HttpSession session = request.getSession();
    session.setAttribute("islogin", true);
    session.setAttribute("user", authentication.getName());
    log.info(authentication.getName());

    response.sendRedirect("/loginSuccess");
  }

}
