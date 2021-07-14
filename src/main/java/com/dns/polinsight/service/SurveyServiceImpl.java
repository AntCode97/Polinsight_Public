package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final MongoTemplate mongoTemplate;

  @Value("${custom.api.accessToken}")
  private String accessToken;

  @Value("${custom.api.url}")
  private String baseURL;

  @Override
  public Survey save(Survey survey) {
    return null;
  }

  @Cacheable(cacheNames = "allSurveys")
  @Override
  public List<Survey> findAll() {
    return null;
  }

  @Override
  public Survey findById(Survey survey) {
    return null;
  }

  @Override
  public Optional<Survey> update(Survey survey) {
    return Optional.empty();
  }

  @Override
  public void delete(Survey survey) {

  }

  @Cacheable(cacheNames = "surveyList")
  public List<Survey> getSurveyListFromSM() {
    HttpHeaders header = new HttpHeaders();
    header.setBearerAuth(accessToken);
    String requestURL = "/surveys";
    new RestTemplate().exchange(baseURL + requestURL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Survey>>() {});
    // TODO: 2021-07-09 : 헤더에 엑세스토큰 넣기
    return mongoTemplate.save((List<Survey>) new RestTemplate().exchange(baseURL + "surveys", HttpMethod.GET, null, new ParameterizedTypeReference<List<Survey>>() {}));
  }

  /*
   * 매 한시간마다 서베이몽키에 접근해, 서베이 목록 수집
   * // TODO: 2021-07-15  : 데이터 파싱 및 저장 필요
   * */
  @Override
  @Scheduled(cron = "0 0 0/1 * * *")
  public void getSurveysWithSchedular() {
    log.info("scheduler start");
    HttpHeaders header = new HttpHeaders();
    header.setBearerAuth(accessToken);
    List<Survey> surveys = (List<Survey>) new RestTemplate().exchange(baseURL + "/surveys", HttpMethod.GET, new HttpEntity<>(header), new ParameterizedTypeReference<List<Survey>>() {});
    mongoTemplate.save(surveys);
    log.info("scheduler end");
  }

}
