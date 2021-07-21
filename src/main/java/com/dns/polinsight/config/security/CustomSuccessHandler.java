package com.dns.polinsight.config.security;

import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  private final HttpSession session;

  private final UserRepository repository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//    session.setMaxInactiveInterval(5 * 60); // 세션 만료시간 5분
    response.setStatus(HttpStatus.OK.value());
    SessionUser sessionUser = new SessionUser(repository.findUserByEmail(authentication.getName()).get());
    session.setAttribute("user", sessionUser);
    response.sendRedirect("/");
  }


}
