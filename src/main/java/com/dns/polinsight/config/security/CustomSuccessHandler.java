package com.dns.polinsight.config.security;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("customSuccessHandler")
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String[] emails = authentication.getName().split("@");
    request.getSession().setMaxInactiveInterval(30 * 60);
    request.getSession().setAttribute("user", new UserDto((User) authentication.getPrincipal()));
    log.info("{} login", ((User) authentication.getPrincipal()).getEmail());
    response.sendRedirect("/");
    response.setStatus(HttpStatus.OK.value());
  }


}
