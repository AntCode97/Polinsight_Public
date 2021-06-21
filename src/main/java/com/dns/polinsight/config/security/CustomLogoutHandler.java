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

    // TODO: 2021/06/12 Kakao OAuth2 로그아웃 기능 요청 구현?? -- 생각 필요
    response.sendRedirect("/");
  }

  private void oAuth2UserLogout(HttpServletRequest request) {

  }

  private boolean kakaoLogout() {
    String state = "";
    String response = "";
    String logoutUrl = "https://kauth.kakao.com";
    String params = "/oauth/logout?client_id={REST_API_KEY}&logout_redirect_uri={LOGOUT_REDIRECT_URI}";

    return state == response;
  }

}
