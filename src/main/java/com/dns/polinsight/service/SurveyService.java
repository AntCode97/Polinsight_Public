package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.dto.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Page<Survey> findAll(Pageable pageable);

  Page<Survey> findAll(Pageable pageable, String regex);

  Survey findById(long surveyId);

  Survey update(Survey survey);

  List<Survey> getSurveyListAndSyncPerHour();


  void deleteSurveyById(long surveyId);

  List<Survey> findSurveysByEndDate(LocalDate endDate);

  List<Survey> findSurveysByTitleRegex(String titleRegex, Pageable pageable);

  Optional<Survey> findSurveyById(long surveyId);

  Optional<Survey> findSurveyBySurveyId(long surveyId);

  long countAllSurvey();


  void adminSurveyUpdate(long id, long point, String create, String end, String progressType);

  List<SurveyDto> findAllSurveyWithCollector(Pageable pageable);

}
