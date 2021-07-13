package com.dns.polinsight.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@RequiredArgsConstructor
public class ChangePwdDto {

  @Id
  private final String email;

  private final String name;

  private final String hash;

}
