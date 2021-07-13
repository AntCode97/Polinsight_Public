package com.dns.polinsight.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

  /*
   * 인증 하지 않은 상황에서 호출시 발생
   * */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    // 에러가 발생하였을 때, 인덱스 페이지로 리다이렉팅
    request.setAttribute("msg", "로그인 먼저 해주세요");
    request.getRequestDispatcher("/405").forward(request, response);

  }

}
