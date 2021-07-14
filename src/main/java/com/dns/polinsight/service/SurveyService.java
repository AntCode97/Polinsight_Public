package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;


public interface SurveyService {

  Survey save(Survey survey);

  List<Survey> findAll();

  Survey findById(Survey survey);

  Optional<Survey> update(Survey survey);

  void delete(Survey survey);

  List<Survey> getSurveyListFromSM();

  /*
   * 한시간마다 서베이몽키에 접근해, 서베이 목록 수집
   * */
  @Scheduled(cron = "0 0 0/1 * * *")
  void getSurveysWithSchedular();

}
