package com.dns.polinsight.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    //    세션 초기화
    request.getSession().invalidate();

    // TODO: 2021-06-01 : OAuth2 로그아웃 기능 구현
    response.sendRedirect("/");
  }

  public void oAuth2UserLogout(HttpServletRequest request) {

  }

}
