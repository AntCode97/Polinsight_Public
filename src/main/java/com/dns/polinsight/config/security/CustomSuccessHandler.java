package com.dns.polinsight.config.security;

import com.dns.polinsight.object.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    response.getWriter().write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ResponseObject.builder()
                                                                                                                    .statuscode(HttpStatus.OK.value())
                                                                                                                    .data(1)
                                                                                                                    .msg("success")
                                                                                                                    .build()));

  }

}
