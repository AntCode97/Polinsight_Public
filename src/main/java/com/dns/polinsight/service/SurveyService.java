package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.mapper.SurveyMapping;
import com.dns.polinsight.types.ProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
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

  List<Survey> findSurveysByTitleRegex(String titleRegex, Pageable pageable);

  Optional<Survey> findSurveyById(long surveyId);

  Optional<Survey> findSurveyBySurveyId(long surveyId);

  // TODO: 2021-08-13 테스트 필요 --> 테스트 시 테스트용 서버 만들어서 사용할 것
  // TODO: 2021-08-18 : 스케줄 주석 제거
  @Transactional
  //  @Scheduled(cron = "0 0 0/1 * * *")
  void getSurveyListAndSyncPerHour();

  void deleteSurveyById(long surveyId);

  void adminSurveyUpdate(long id, long point, String create, String end, String progressType);


}
