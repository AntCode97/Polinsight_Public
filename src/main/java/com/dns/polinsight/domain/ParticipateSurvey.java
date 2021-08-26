package com.dns.polinsight.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name = "participate_survey")
public class ParticipateSurvey {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @JoinColumn(name = "user_id")
  @ManyToOne(targetEntity = User.class)
  private User user;

  @JoinColumn(name = "survey_id")
  @ManyToOne(targetEntity = Survey.class)
  private Survey survey;

  private LocalDateTime participatedAt;

  private Long surveyPoint;

  private String hash;

  @Setter
  private Boolean finished;

}
