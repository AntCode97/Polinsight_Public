package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Survey findById(Survey survey);

  Survey update(Survey survey);

  List<Survey> getSurveyListAndSyncPerHour();

  List<Survey> getUserParticipateSurvey(User user);

  void deleteSurveyById(Long surveyId);

  List<Survey> findSurveysByEndDate(LocalDateTime endDate);

  List<Survey> findSurveysByTitleRegex(String titleRegex);

  Optional<Survey> findSurveyById(long surveyId);

  long countAllSurvey();
}
