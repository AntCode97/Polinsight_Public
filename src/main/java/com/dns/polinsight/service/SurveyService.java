package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.mapper.SurveyMapping;
import com.dns.polinsight.types.ProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Optional<SurveyMapping> findSurveyById(Long id);

  Page<SurveyMapping> findAll(Pageable pageable);

  Page<SurveyMapping> findAllByTypes(Pageable pageable, ProgressType type);

  Page<SurveyMapping> findAllSurveysByProgressTypeNotLike(Pageable pageable, ProgressType type);

  Page<SurveyMapping> findAllAndRegex(Pageable pageable, String regex);

  Page<SurveyMapping> findAllByTypesAndRegex(Pageable pageable, ProgressType type, String regex);

  Page<Survey> findAll(Pageable pageable, String regex);

  Survey findById(long surveyId);

  Survey update(Survey survey);

  List<Survey> findSurveysByTitleRegex(String titleRegex, Pageable pageable);

  Optional<Survey> findSurveyById(long surveyId);

  Optional<Survey> findSurveyBySurveyId(Long surveyId);

  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  void getSurveyListAndSyncPerHour();

  void deleteSurveyById(long surveyId);

  int adminSurveyUpdate(long id, long point, String create, String end, String progressType);


}
