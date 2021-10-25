package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.projection.SurveyMapping;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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


  private String originalName;

  private String thumbnail;

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
    this.originalName = survey.getOriginalName();
  }

  public static SurveyDto of(SurveyMapping mapping) {
    return SurveyDto.builder()
                    .id(mapping.getId())
                    .point(mapping.getPoint())
                    .surveyId(mapping.getSurveyId())
                    .count(mapping.getQuestionCount())
                    .progress(mapping.getProgress())
                    .minimumTime(mapping.getMinimumTime())
                    .createdAt(mapping.getCreatedAt())
                    .endAt(mapping.getEndAt())
                    .participateUrl(mapping.getParticipateUrl())
                    .title(mapping.getTitle())
                    .originalName(mapping.getOriginalName())
                    .thumbnail(mapping.getThumbnail())
                    .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
