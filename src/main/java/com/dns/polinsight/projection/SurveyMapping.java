package com.dns.polinsight.projection;

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

  String getThumbnail();

  LocalDate getEndAt();

  String getOriginalName();

}
