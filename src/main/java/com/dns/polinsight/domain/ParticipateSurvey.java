package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.ParticipateSurveyDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
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


  private static final long serialVersionUID = -8790118046300244268L;

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
