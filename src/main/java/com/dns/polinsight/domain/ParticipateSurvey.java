package com.dns.polinsight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name = "participate_survey")
public class ParticipateSurvey implements Serializable {

  private static final long serialVersionUID = 2771063029297680262L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @ManyToOne(targetEntity = User.class)
  private User user;

  //  @JsonIgnore
  @JoinColumn(name = "survey_Id", referencedColumnName = "survey_id")
  @ManyToOne(targetEntity = Survey.class)
  private Survey survey;

  private LocalDateTime participatedAt;

  private Long surveyPoint;

  private String hash;

  @Setter
  private Boolean finished;

}
