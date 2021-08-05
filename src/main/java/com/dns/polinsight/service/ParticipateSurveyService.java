package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParticipateSurveyService {

  List<ParticipateSurvey> findByUserId(long userId);

  Optional<ParticipateSurvey> findBySurveyUserPairHash(String hash);

  List<ParticipateSurvey> findByUserIdAndParticipateAt(long userId, LocalDateTime participateAt);

  List<ParticipateSurvey> findByUserIdAndSurveyPoint(long userId, long point);

  ParticipateSurvey saveParticipateSurvey(ParticipateSurvey participateSurvey);

  ParticipateSurvey getHashByEmail(String email);

  ParticipateSurvey getPoint(User user);

  ParticipateSurvey getParticipateSurveyByUserId(long userId);

  ParticipateSurvey getParticipateSurveyByUserEmail(String email);


  void saveAndUpdate(ParticipateSurvey participateSurvey);

}
