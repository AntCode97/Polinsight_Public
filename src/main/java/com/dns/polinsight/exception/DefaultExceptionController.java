package com.dns.polinsight.exception;

import com.dns.polinsight.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

import static com.dns.polinsight.utils.ApiUtils.error;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionController {

  @ExceptionHandler(PointNotUpdateException.class)
  public ApiUtils.ApiResult<?> pointNotUpdateHandler(Exception e) {
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }


  @ExceptionHandler(MessagingException.class)
  public ApiUtils.ApiResult<?> messagingExceptionHandler(MessagingException e) {
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(TooManyRequestException.class)
  public ApiUtils.ApiResult<?> tooManyRequestExceptionHandler(TooManyRequestException e) {
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(Exception.class)
  public ApiUtils.ApiResult<?> handleException(Exception e) {
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(NoSuchAlgorithmException.class)
  public ApiUtils.ApiResult<?> handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(SurveyNotFoundException.class)
  public ApiUtils.ApiResult<?> handleSurveyNotFound(SurveyNotFoundException e) {
    return error("Survey Not Found", HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(WrongAccessException.class)
  public ModelAndView handleWrongAccessException(WrongAccessException e) {
    ModelAndView mv = new ModelAndView("redirect:/");
    mv.addObject("msg", e.getMessage());
    return mv;
  }

}
