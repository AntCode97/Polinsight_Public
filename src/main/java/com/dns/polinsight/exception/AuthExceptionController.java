package com.dns.polinsight.exception;

import com.dns.polinsight.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.dns.polinsight.utils.ApiUtils.error;

@Slf4j
@ControllerAdvice
public class AuthExceptionController {

  @ExceptionHandler(BadCredentialsException.class)
  public ApiUtils.ApiResult<?> handleBadCredential(BadCredentialsException e) {
    return error(e.getMessage(), 400);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ApiUtils.ApiResult<?> handleUserNameNotFoundException(UsernameNotFoundException e) {
    return error(e.getMessage(), 400);
  }

}
