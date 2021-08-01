package com.dns.polinsight.config.security;

import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.object.ResponseObject;
import com.dns.polinsight.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  //  private final HttpSession session;

  private final UserRepository repository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    request.getSession().setMaxInactiveInterval(30 * 60); // 세션 만료시간 30분
    response.setStatus(HttpStatus.OK.value());
    SessionUser sessionUser = new SessionUser(repository.findUserByEmail(authentication.getName()).get());
    request.getSession().setAttribute("user", sessionUser);
    response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseObject.builder()
                                                                                   .response(true)
                                                                                   .build()).trim());
  }


}
