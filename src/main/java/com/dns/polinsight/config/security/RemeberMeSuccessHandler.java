package com.dns.polinsight.config.security;

import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.repository.UserRepository;
import com.dns.polinsight.types.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component("rememberMeSuccessHandler")
public class RemeberMeSuccessHandler implements AuthenticationSuccessHandler {

  private final UserRepository repository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String[] emails = authentication.getName().split("@");
    request.getSession().setMaxInactiveInterval(30 * 60);
    request.getSession().setAttribute("user", new UserDto(repository.findUserByEmail(Email.builder().account(emails[0]).domain(emails[1]).build()).get()));
    response.setStatus(HttpServletResponse.SC_OK);
    response.sendRedirect("/");
  }

}
