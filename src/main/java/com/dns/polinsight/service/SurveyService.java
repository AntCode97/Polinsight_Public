package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Page<Survey> findAll(Pageable pageable);

  Survey findById(long surveyId);

  Survey update(Survey survey);

  List<Survey> getSurveyListAndSyncPerHour();

  Set<Survey> getUserParticipateSurvey(User user);

  void deleteSurveyById(long surveyId);

  List<Survey> findSurveysByEndDate(LocalDateTime endDate);

  List<Survey> findSurveysByTitleRegex(String titleRegex, Pageable pageable);

  Optional<Survey> findSurveyById(long surveyId);

  Optional<Survey> findSurveyBySurveyId(long surveyId);

  long countAllSurvey();


  void adminSurveyUpdate(long id, long point, String create, String end, String progressType);

  List<SurveyDto> findAllSurveyWithCollector(Pageable pageable);

}
