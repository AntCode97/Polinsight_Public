package com.dns.polinsight.types;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum PointRequestProgressType {
  REQUESTED("요청"), FINISHED("완료"), ERROR("오류");

  String name;

  public String getName() {
    return name;
  }
}
