package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.ParticipateSurvey;
import lombok.*;

import java.beans.ConstructorProperties;

@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@ConstructorProperties({"email", "title", "participateAt", "point", "finished"})})
@Setter
@Getter
@ToString
public class ParticipateSurveyDto {

  private String email;

  private String title;

  private String participatedAt;

  private Long point;

  private Boolean finished;

  public ParticipateSurveyDto(ParticipateSurvey participateSurvey) {
    this.title = participateSurvey.getSurvey().getTitle();
    this.email = participateSurvey.getUser().getEmail().toString();
    this.point = participateSurvey.getSurveyPoint();
    this.participatedAt = String.valueOf(participateSurvey.getParticipatedAt());
    this.finished = participateSurvey.getFinished();
  }

}
