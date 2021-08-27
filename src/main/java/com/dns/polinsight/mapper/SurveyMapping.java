package com.dns.polinsight.mapper;

import com.dns.polinsight.types.ProgressType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

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

  @JsonIgnore
  LocalDate getCreatedat();

  @JsonIgnore
  LocalDate getEndat();

  default String getEndAt() {
    return getEndat() == null ? LocalDate.now().toString() : getEndat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  String getParticipateurl();

  Long getQuestionCount();

  //  @JsonIgnore
  //  Collector getCollector();

  //  @JsonIgnore
  //  SurveyStatus getStatus();


}
