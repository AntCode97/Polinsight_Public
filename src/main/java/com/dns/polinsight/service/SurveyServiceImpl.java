package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.SurveyMonkeyDTO;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.repository.SurveyRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyRepository surveyRepository;

  @Value("${custom.api.accessToken}")
  private String accessToken;

  @Value("${custom.api.url}")
  private String baseURL;

  @Override
  public Survey save(Survey survey) {
    return surveyRepository.save(survey);
  }

  @Cacheable(cacheNames = "surveyList")
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
   * 종료일은 따로 제공하지 않음 --> 어드민으로부터 받아야할듯
   * */
  @Override
  @Cacheable(cacheNames = "surveyList")
  @Scheduled(cron = "0 0 0/1 * * *")
  public List<Survey> getSurveyListAndSyncPerHour() throws Exception {
    final String additionalUrl = "/surveys?include=date_created,date_modified,preview";
    HttpHeaders header = new HttpHeaders();
    header.setBearerAuth(accessToken);
    HttpEntity<Object> httpEntity = new HttpEntity<>(header);
    ResponseEntity<Map> map = new RestTemplate().exchange(baseURL + additionalUrl, HttpMethod.GET, httpEntity, Map.class);
    List<Map<String, String>> tmplist = (List<Map<String, String>>) map.getBody().get("data");
    List<Survey> surveyList = tmplist.stream().map(objmap -> SurveyMonkeyDTO.builder()
                                                                            .id(Long.valueOf(objmap.get("id")))
                                                                            .title(objmap.get("title"))
                                                                            .nickname(objmap.get("nickname"))
                                                                            .href(objmap.get("href"))
                                                                            .createdAt(LocalDateTime.parse(objmap.get("date_created")))
                                                                            .modifiedAt(LocalDateTime.parse(objmap.get("date_modified")))
                                                                            .build()).map(SurveyMonkeyDTO::toSurvey).collect(Collectors.toList());
    surveyList = surveyList.parallelStream().map(survey -> {
      survey.getStatus().setProgressType(survey.getEndAt());
      survey.getStatus().setSurvey(survey);
      return survey;
    }).collect(Collectors.toList());

    surveyRepository.saveAllAndFlush(surveyList);

    log.info("survey sync success");
    return surveyList;
  }

  @Override
  public List<Survey> getUserParticipateSurvey(User user) {
    // TODO: 2021-07-21 수정 필요
    StringTokenizer st = new StringTokenizer(user.getParticipateSurvey());
    List<Survey> list = new ArrayList<>();
    while (st.hasMoreTokens()) {
      list.add(this.findById(Survey.builder().id(Long.parseLong(st.nextToken())).build()));
    }
    return list;
  }


  /*
   * get details for custom variables
   * */
  private Survey getSurveyDetails(Survey basicSurvey, HttpEntity<Object> header) {
    StringBuffer sb = new StringBuffer("/surveys/");
    sb.append(basicSurvey.getSurveyId());
    sb.append("/details");

    Survey survey = null;
    new RestTemplate().exchange(baseURL + sb, HttpMethod.GET, header, Map.class);
    return survey;
  }

  @Override
  public void deleteSurveyById(Long surveyId) {
    surveyRepository.delete(Survey.builder().id(surveyId).build());
  }

  @Override
  public List<Survey> findSurveyByRgex(String regex) {
    if (regex.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
      return surveyRepository.findSurveysByEndAtLessThan(LocalDateTime.parse(regex));
    } else {
      return surveyRepository.findSurveysByTitleLike(regex);
    }
  }

}
