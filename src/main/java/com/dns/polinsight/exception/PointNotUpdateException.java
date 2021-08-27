package com.dns.polinsight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PointNotUpdateException extends RuntimeException {

  public PointNotUpdateException() {
  }

  public PointNotUpdateException(String message) {
    super(message);
  }

}
