package com.dns.polinsight.object;

import com.dns.polinsight.domain.Collector;
import com.dns.polinsight.domain.SurveyStatus;
import com.dns.polinsight.types.ProgressType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface SurveyMapping {

  Long getId();

  String getTitle();

  Long getPoint();

  Long getSurveyId();

  ProgressType getProgress();

  Integer getMinimumtime();

  default String getCreatedAt() {
    return getCreatedat() == null ? LocalDate.now().toString() : getCreatedat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  LocalDate getCreatedat();

  LocalDate getEndat();

  default String getEndAt() {
    return getEndat() == null ? LocalDate.now().toString() : getEndat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  default String getParticipateurl() {
    return getCollector().getParticipateUrl();
  }

  Long getQuestionCount();

  @JsonIgnore
  Collector getCollector();

  @JsonIgnore
  SurveyStatus getStatus();


}
