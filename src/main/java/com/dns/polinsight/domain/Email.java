package com.dns.polinsight.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Email {

  private String id;

  private String domain;

  @Override
  public String toString() {
    return id + "@" + domain;
  }

}
