package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.dto.SurveyMonkeyDTO;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.repository.MongoSurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final MongoSurveyRepository surveyRepository;

  @Value("${custom.api.accessToken}")
  private String accessToken;

  @Value("${custom.api.url}")
  private String baseURL;

  @Override
  public Survey save(Survey survey) {
    return surveyRepository.save(survey);
  }

  @Cacheable(cacheNames = "allSurveys")
  @Override
  public List<Survey> findAll() {
    return surveyRepository.findAll();
  }

  @Override
  public Survey findById(Survey survey) {
    return surveyRepository.findById(survey.getSurveyId()).orElseThrow(SurveyNotFoundException::new);
  }

  @Override
  public Survey update(Survey survey) {
    return surveyRepository.save(survey);
  }

  /*
   * 매 한시간마다 서베이몽키에 접근해, 서베이 목록 수집
   * */
  @Override
  @Cacheable(cacheNames = "surveyList")
  @Scheduled(cron = "0 0 0/1 * * *")
  public List<Survey> getSurveyListAndSyncWithScheduler() throws Exception {
    HttpHeaders header = new HttpHeaders();
    header.setBearerAuth(accessToken);
    ResponseEntity<Map> map = new RestTemplate().exchange(baseURL + "/surveys", HttpMethod.GET, new HttpEntity<>(header), Map.class);
    List<Map<String, String>> tmplist = (List<Map<String, String>>) map.getBody().get("data");
    List<Survey> surveyList = tmplist.stream().map(objmap -> SurveyMonkeyDTO.builder()
                                                                            .id(Long.valueOf(objmap.get("id")))
                                                                            .title(objmap.get("title"))
                                                                            .nickname(objmap.get("nickname"))
                                                                            .href(objmap.get("href"))
                                                                            .build()).map(SurveyMonkeyDTO::toSurvey).collect(Collectors.toList());
    surveyList.forEach(surveyRepository::save);
    log.info("survey sync success");
    return surveyList;
  }

}
