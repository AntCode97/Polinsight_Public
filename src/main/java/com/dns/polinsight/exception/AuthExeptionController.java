package com.dns.polinsight.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class AuthExeptionController {

  @ExceptionHandler(BadCredentialsException.class)
  public ModelAndView handleBadCredential(HttpServletRequest request, HttpServletResponse response, Exception e) {
    ModelAndView mv = new ModelAndView();
    logExceptionInfo(e);
    mv.addObject("msg", e.getMessage());
    mv.addObject("code", 6000);
    log.info(this.getClass().getCanonicalName() + ", Error Code: 6000, Cause: " + e.getCause());
    return mv;
  }

  private void logExceptionInfo(Exception e) {
    log.error("Msg: {}", e.getMessage());
    log.error("Cause: {}", e.getCause());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ModelAndView handleUserNameNotFoundException(Exception e) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("msg", e.getMessage());
    mv.addObject("code", 6001);
    return mv;
  }
  
}
