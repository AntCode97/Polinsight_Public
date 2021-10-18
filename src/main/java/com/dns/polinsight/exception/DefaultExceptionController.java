package com.dns.polinsight.exception;

import com.dns.polinsight.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.dns.polinsight.utils.ApiUtils.error;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionController {

  @ExceptionHandler(PointNotUpdateException.class)
  public ApiUtils.ApiResult<?> pointNotUpdateHandler(Exception e, HttpServletResponse response) throws IOException {
    log.error("[PointNotUpdateException]", e);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "PointNotUpdateException Error");
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }


  @ExceptionHandler(MessagingException.class)
  public ApiUtils.ApiResult<?> messagingExceptionHandler(MessagingException e, HttpServletResponse response) throws IOException {
    log.error("[MessagingException]", e);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "MessagingException Error");
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(TooManyRequestException.class)
  public ApiUtils.ApiResult<?> tooManyRequestExceptionHandler(TooManyRequestException e, HttpServletResponse response) throws IOException {
    log.error("[TooManyRequestException]", e);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "TooManyRequestException Error");
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(Exception.class)
  public ApiUtils.ApiResult<?> handleException(Exception e, HttpServletResponse response) throws IOException {
    log.error("[Exception]", e);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception Error");
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(NoSuchAlgorithmException.class)
  public ApiUtils.ApiResult<?> handleNoSuchAlgorithmException(NoSuchAlgorithmException e, HttpServletResponse response) throws IOException {
    log.error("[PointNotUpdateException]", e);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "PointNotUpdateException Error");
    return error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(SurveyNotFoundException.class)
  public ApiUtils.ApiResult<?> handleSurveyNotFound(SurveyNotFoundException e, HttpServletResponse response) throws IOException {
    log.error("[SurveyNotFoundException]", e);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SurveyNotFoundException Error");
    return error("Survey Not Found", HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @ExceptionHandler(AlreadyParticipateSurveyException.class)
  public ApiUtils.ApiResult<?> handleAlreadyParticipateSurveyException() {
    return error("이미 참여한 설문입니다.", 6789);
  }

}
