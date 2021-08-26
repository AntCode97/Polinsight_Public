package com.dns.polinsight.exception;

import java.util.function.Supplier;

public class SurveyNotFoundException extends RuntimeException {

  public SurveyNotFoundException() {
    super();
  }

  public SurveyNotFoundException(String message) {
    super(message);
  }


}
