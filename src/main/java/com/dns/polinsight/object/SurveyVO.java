package com.dns.polinsight.object;

import com.dns.polinsight.types.ProgressType;

public interface SurveyVO {

  Long getId();

  String getTitle();

  Long getPoint();

  Long getSurveyId();

  ProgressType getProgress();

  Integer getMinimumtime();

  String getCreatedat();

  String getEndat();

  String getParticipateurl();

  Long getCount();

}
