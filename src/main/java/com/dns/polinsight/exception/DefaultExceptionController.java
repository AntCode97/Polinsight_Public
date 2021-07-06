package com.dns.polinsight.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class DefaultExceptionController {

  @ExceptionHandler(PointNotUpdateException.class)
  public ModelAndView pointNotUpdateHandler(Exception e) {
    ModelAndView mv = new ModelAndView();
    log.info("User don't get point caused: " + e);
    mv.addObject("code", 8000);
    mv.addObject("msg", e.getMessage());
    return mv;
  }

}
