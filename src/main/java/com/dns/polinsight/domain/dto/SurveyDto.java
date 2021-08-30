package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SurveyDto {

  private Long id;

  private String title;

  private String participateUrl;

  private Long point;

  private Long surveyId;

  private Integer minimumTime;

  private LocalDate createdAt;

  private LocalDate endAt;

  @Enumerated(EnumType.STRING)
  private CollectorStatusType status;

  private Long count;

  @Enumerated(EnumType.STRING)
  private ProgressType progress;

  public SurveyDto(Survey survey) {
    this.id = survey.getId();
    this.point = survey.getPoint();
    this.surveyId = survey.getSurveyId();
    this.count = survey.getCollector().getResponseCount();
    this.progress = survey.getStatus().getProgress();
    this.minimumTime = survey.getStatus().getMinimumTime();
    this.createdAt = survey.getCreatedAt();
    this.endAt = survey.getEndAt();
    this.participateUrl = survey.getCollector().getParticipateUrl();
    this.status = survey.getCollector().getStatus();
    this.title = survey.getTitle();
  }


}
