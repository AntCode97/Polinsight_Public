package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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
    // TODO: 2021-07-09 : 헤더에 엑세스토큰 넣기
    List<Survey> surveys = (List<Survey>) new RestTemplate().exchange(baseURL + "surveys", HttpMethod.GET, null, new ParameterizedTypeReference<List<Survey>>() {});
    mongoTemplate.save(surveys);
    return surveys;
  }

}
