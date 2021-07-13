package com.dns.polinsight.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionController {

  @ExceptionHandler(PointNotUpdateException.class)
  public ResponseEntity<?> pointNotUpdateHandler(Exception e) {
    Map<String, Object> map = new HashMap<>();
    log.info("User don't get point caused: " + e);
    map.put("code", 8000);
    map.put("msg", e.getMessage());
    return ResponseEntity.ok(map);
  }


  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<?> messagingExceptionHandler(MessagingException e) {
    Map<String, Object> map = new HashMap<>();
    map.put("code", 8001);
    map.put("msg", e.getMessage());
    return ResponseEntity.ok(map);
  }

}
