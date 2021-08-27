package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.mapper.SurveyMapping;
import com.dns.polinsight.types.ProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Page<SurveyMapping> findAll(Pageable pageable);

  Page<SurveyMapping> findAllByTypes(Pageable pageable, ProgressType type);


  Page<SurveyMapping> findAllSurveysByProgressTypeNotLike(Pageable pageable, ProgressType type);

  Page<SurveyMapping> findAllAndRegex(Pageable pageable, String regex);

  Page<SurveyMapping> findAllByTypesAndRegex(Pageable pageable, ProgressType type, String regex);

  Page<Survey> findAll(Pageable pageable, String regex);

  Survey findById(long surveyId);

  Survey update(Survey survey);

  List<Survey> getSurveyListAndSyncPerHour();


  void deleteSurveyById(long surveyId);

  List<Survey> findSurveysByEndDate(LocalDate endDate);

  List<Survey> findSurveysByTitleRegex(String titleRegex, Pageable pageable);

  Optional<Survey> findSurveyById(long surveyId);

  Optional<Survey> findSurveyBySurveyId(long surveyId);

  long countAllSurvey(String type);


  void adminSurveyUpdate(long id, long point, String create, String end, String progressType);


}
