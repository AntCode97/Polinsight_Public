package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;

import java.util.List;
import java.util.Optional;

public interface ParticipateSurveyService {

  List<ParticipateSurvey> findAll();

  List<ParticipateSurvey> findAllByUserId(long userId);


  Optional<ParticipateSurvey> findBySurveyUserPairHash(String hash);


  ParticipateSurvey saveParticipateSurvey(ParticipateSurvey participateSurvey);

  ParticipateSurvey getParticipateSurveyByUserEmail(String email);

  ParticipateSurvey saveAndUpdate(ParticipateSurvey participateSurvey);

}
