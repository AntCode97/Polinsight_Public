package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Point {

  @Id
  private Long id;

  private Long uid;

  @Email
  private String email;

  private Long pointValue;

  private String hash;

  private Long surveyId;

}
