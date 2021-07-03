package com.dns.polinsight.config.security;

import com.dns.polinsight.object.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.info("************************************************************************************" + exception.getMessage());
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ResponseObject.builder()
                                                                                                                    .statuscode(HttpStatus.OK.value())
                                                                                                                    .msg("login failed")
                                                                                                                    .data(exception.getMessage())
                                                                                                                    .build()));
  }

}
