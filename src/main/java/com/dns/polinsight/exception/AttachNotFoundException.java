package com.dns.polinsight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class AttachNotFoundException extends RuntimeException {

  public AttachNotFoundException() {
    super();
  }

  public AttachNotFoundException(String message) {
    super(message);
  }

}

