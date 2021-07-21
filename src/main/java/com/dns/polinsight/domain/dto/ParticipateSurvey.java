package com.dns.polinsight.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParticipateSurvey implements Serializable {

  @Id
  private Long id;

  private Long uid;

  private String surveyId;

  private LocalDateTime participatedAt;

}
