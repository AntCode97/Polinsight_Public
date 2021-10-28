package com.dns.polinsight.exception;

import com.dns.polinsight.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dns.polinsight.utils.ApiUtils.error;

@Slf4j
@RestControllerAdvice
public class AuthExceptionController {

  @ExceptionHandler({BadCredentialsException.class, UnAuthorizedException.class})
  public ApiUtils.ApiResult<?> handleBadCredential(BadCredentialsException e) {
    log.error(e.getMessage());
    return error("Authorized Error", HttpStatus.UNAUTHORIZED.value());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ApiUtils.ApiResult<?> handleUserNameNotFoundException(UsernameNotFoundException e) {
    log.error(e.getMessage());
    return error("Authorized Error", HttpStatus.UNAUTHORIZED.value());
  }

  @ExceptionHandler({AccessDeniedException.class, WrongAccessException.class})
  public ApiUtils.ApiResult<?> handleAccessDeniedException(AccessDeniedException e, HttpServletResponse response) throws IOException {
    response.sendRedirect("/denied");
    log.error(e.getMessage());
    return error("Authorized Error", HttpStatus.UNAUTHORIZED.value());
  }

}
