package com.dns.polinsight.service;

import com.dns.polinsight.domain.ParticipateSurvey;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParticipateSurveyService {

  List<ParticipateSurvey> findAll();

  List<ParticipateSurvey> findAllByUserId(long userId, Pageable pageable);

  List<ParticipateSurvey> findAllByUserId(long userId);

  Optional<ParticipateSurvey> findBySurveyUserPairHash(String hash);

  long countExistParticipateSurvey();

  ParticipateSurvey saveParticipateSurvey(ParticipateSurvey participateSurvey);

  ParticipateSurvey getParticipateSurveyByUserEmail(String email);

  ParticipateSurvey saveAndUpdate(ParticipateSurvey participateSurvey);

  void updateParticipateSurveyById(long pSurveyId);

  Boolean isExistParticipates(Long surveyId);

}
