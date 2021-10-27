package com.dns.polinsight.projection;

import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;

import java.time.LocalDate;
import java.util.Set;

public interface SurveyMapping {

  // Collector
  Long getCollectorId();

  Long getCollectorCollectorId();

  String getCollectorName();

  String getCollectorHref();

  String getCollectorParticipateUrl();

  Long getCollectorResponseCount();

  CollectorStatusType getCollectorStatus();

  // Survey

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

  Set<String> getVariables();

  Long getCount();

}
