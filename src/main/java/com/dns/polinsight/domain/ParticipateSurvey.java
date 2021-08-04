package com.dns.polinsight.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ParticipateSurvey implements Serializable {

  private static final long serialVersionUID = 2771063029297680262L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  private Long surveyId;

  private LocalDateTime participatedAt;

  private Long surveyPoint;

  private String hash;

  @Setter
  private Boolean finished;

}
