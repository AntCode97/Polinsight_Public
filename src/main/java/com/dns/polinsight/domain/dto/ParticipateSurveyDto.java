package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.types.Email;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParticipateSurveyDto {

  private Long userId;

  private Email email;

  private Long surveyId;

  private String title;

  private LocalDateTime participatedAt;

  private Long point;

  private String hash;

  private Boolean finished;

  public ParticipateSurveyDto(ParticipateSurvey participateSurvey) {
    this.title = participateSurvey.getSurvey().getTitle();
    this.userId = participateSurvey.getUser().getId();
    this.email = participateSurvey.getUser().getEmail();
    this.surveyId = participateSurvey.getSurvey().getSurveyId();
    this.point = participateSurvey.getSurveyPoint();
    this.participatedAt = participateSurvey.getParticipatedAt();
    this.hash = participateSurvey.getHash();
    this.finished = participateSurvey.getFinished();
  }

}
