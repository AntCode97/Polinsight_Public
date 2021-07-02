package com.dns.polinsight.object;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ResponseObject {

  private final int statuscode;

  private final Object data;

  private final String msg;

}