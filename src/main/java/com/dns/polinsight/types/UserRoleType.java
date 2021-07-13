package com.dns.polinsight.types;

public enum UserRoleType {
  USER("일반", 100),
  PANEL("일반 패널", 200),
  BEST("우수 패널", 300),
  ADMIN("관리자", 400);

  private final String name;

  private final int code;

  UserRoleType(String name, int code) {
    this.name = name;
    this.code = code;
  }
}
