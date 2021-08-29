package com.dns.polinsight.mapper;

import com.dns.polinsight.types.ProgressType;

import java.time.LocalDate;

public interface SurveyMapping {

  Long getId();

  String getTitle();

  Long getPoint();

  Long getSurveyId();

  ProgressType getProgress();

  Integer getMinimumTime();

  String getParticipateUrl();

  Long getQuestionCount();

  LocalDate getCreatedAt();

  default String getCreate() {
    if (getCreatedAt() != null) {
      return getCreatedAt().toString();
    } else {
      return LocalDate.parse("1900-01-01").toString();
    }
  }

  LocalDate getEndAt();

  default String getEnd() {
    if (getEndAt() != null) {
      return getEndAt().toString();
    } else {
      return LocalDate.parse("1900-01-01").toString();
    }
  }


  //  @JsonIgnore
  //  Collector getCollector();

  //  @JsonIgnore
  //  Survey getSurvey();

}
