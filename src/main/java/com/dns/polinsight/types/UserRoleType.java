package com.dns.polinsight.types;

import lombok.Getter;

@Getter
public enum UserRoleType {
  USER("일반", 100),
  PANEL("일반 패널", 200),
  BEST("우수 패널", 300),
  MANAGER("매니저", 400),
  ADMIN("관리자", 500);

  private final String name;

  private final int code;

  UserRoleType(String name, int code) {
    this.name = name;
    this.code = code;
  }

  public static class ROLES {

    public static final String USER = "ROLE_USER";
    public static final String PANEL = "ROLE_PANEL";

    public static final String BEST = "ROLE_BEST";

    public static final String MANAGER = "ROLE_MANAGER";

    public static final String ADMIN = "ROLE_ADMIN";

  }
}
