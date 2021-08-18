package com.dns.polinsight.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {

  private String account;

  private String domain;

  @Override
  public String toString() {
    return account + "@" + domain;
  }


}
