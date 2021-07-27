package com.dns.polinsight.utils;

import lombok.Getter;
import lombok.Setter;

public class ApiUtils<T> {

  public static <T> ApiResult<T> success(T response) {
    return new ApiResult<T>(true, response, null);
  }

  public static ApiResult<?> error(String message, int status) {
    return new ApiResult<>(false, null, new ApiError(message, status));
  }

  @Getter
  @Setter
  private static class ApiResult<T> {

    private boolean sucess;

    private T response;

    private ApiError apiError;

    public ApiResult(boolean sucess, T response, ApiError apiError) {
      this.sucess = sucess;
      this.response = response;
      this.apiError = apiError;
    }

  }

  @Getter
  @Setter
  private static class ApiError {

    private String message;

    private int status;

    public ApiError(String message, int status) {
      this.message = message;
      this.status = status;
    }

  }

}
