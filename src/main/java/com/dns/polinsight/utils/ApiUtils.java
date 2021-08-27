package com.dns.polinsight.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

public class ApiUtils<T> {

  public static <T> ApiResult<T> success(T response) {
    return new ApiResult<T>(true, response, null);
  }

  public static ApiResult<?> error(String message, int status) {
    return new ApiResult<>(false, null, new ApiError(message, status));
  }

  @Getter
  @Setter
  public static class ApiResult<T> {

    private boolean success;

    private T response;

    private ApiError error;

    public ApiResult(boolean sucess, T response, ApiError apiError) {
      this.success = sucess;
      this.response = response;
      this.error = apiError;
    }

  }

  @Getter
  @Setter
  public static class ApiError {

    private String message;

    private int status;

    public ApiError(String message, int status) {
      this.message = message;
      this.status = status;
    }

  }

}
