package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipateSurveyService {

  List<ParticipateSurvey> findByUserId(long userId);

  List<ParticipateSurvey> findByUserIdAndParticipateAt(long userId, LocalDateTime participateAt);

  List<ParticipateSurvey> findByUserIdAndSurveyPoint(long userId, long point);

}
