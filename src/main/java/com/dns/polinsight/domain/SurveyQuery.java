package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

/*
* 사용자가 설문을 하고, 돌아올때까지의 정보를 갖고있을 엔티티
* */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SurveyQuery {

  @Id
  private Long id;

  private Long uid;

  @Email
  private String email;

  private String hash;

  @ManyToOne
  @JoinColumn(name = "survey_id")
  private Survey surveyId;

}
