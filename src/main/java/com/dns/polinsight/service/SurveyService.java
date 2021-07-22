package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;

import java.util.List;


public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Survey findById(Survey survey);

  Survey update(Survey survey);

  /*
   * 매 한시간마다 서베이몽키에 접근해, 서베이 목록 수집
   * */
  List<Survey> getSurveyListAndSyncPerHour() throws Exception;


  List<Survey> getUserParticipateSurvey(User user);

  void deleteSurveyById(Long surveyId);

  List<Survey> findSurveyByRgex(String regex);

}
