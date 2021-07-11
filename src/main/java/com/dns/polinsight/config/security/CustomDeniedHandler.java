package com.dns.polinsight.config.security;

import com.dns.polinsight.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.info("access denied handler: -------------------");
    /*
     * 인가 거부 처리
     * Access Denied
     * */
    response.setStatus(HttpStatus.FORBIDDEN.value());

    if (accessDeniedException != null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      // 유저가 일반 권한을 갖고 있을 때
      if (authentication != null &&
          ((User) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {

        request.setAttribute("msg", "접근권한이 없는 사용자입니다.");
      } else {
        request.setAttribute("msg", "로그인 권한이 없는 사용자입니다.");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        SecurityContextHolder.clearContext();
      }

    } else {
      log.info(accessDeniedException.getClass().getCanonicalName());
    }

    request.getRequestDispatcher("/denied").forward(request, response);
  }

}
