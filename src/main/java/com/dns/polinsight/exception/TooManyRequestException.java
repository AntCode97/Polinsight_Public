package com.dns.polinsight.exception;

public class TooManyRequestException extends RuntimeException {

  public TooManyRequestException() {
  }

  public TooManyRequestException(String message) {
    super(message);
  }

}
