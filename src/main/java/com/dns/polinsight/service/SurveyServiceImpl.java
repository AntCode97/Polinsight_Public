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
                                                                            //                                                                            .nickname(objmap.get("nickname"))
                                                                            //                                                                            .href(objmap.get("href"))
                                                                            .createdAt(LocalDateTime.parse(objmap.get("date_created")))
                                                                            //                                                                            .modifiedAt(LocalDateTime.parse(objmap.get("date_modified")))
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
    // 서베이 아이디를 통해 서베이의 자세한 정보를 가져오고
    // 가져온 데이터를 이용해, 어떠한 커스텀 변수가 있는지 확인한다 - 문제 갯수, 응답 횟수 까지 알 수 있음
    String apiUrl = "/surveys/" + basicSurvey.getSurveyId() + "/details";
    Survey survey = null;
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + apiUrl, HttpMethod.GET, header, Map.class);
    List<Map<String, String>> tmp = (List<Map<String, String>>) res.getBody();
    return survey;
  }

  private long getTotalResponse(Long surveyId, HttpEntity<Object> header) {
    String api = "/surveys/" + surveyId + "/responses/bulk";  // 설문에 참여한 사람 수, 참여한 사람들 데이터까지 모두 가져올 수 있음
    // 가져온 데이터에서 커스텀 변수를 통해 사용자와 매칭 하고 이를 이용해서 누가 어떤 설문을 완료했는지 기록할 수 있다.
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + api, HttpMethod.GET, header, Map.class);
    return Long.parseLong(String.valueOf(res.getBody().get("total")));
  }

  @Override
  public void deleteSurveyById(Long surveyId) {
    surveyRepository.delete(Survey.builder().id(surveyId).build());
  }

  @Override
  public List<Survey> findSurveysByEndDate(LocalDateTime endDate) {
    return surveyRepository.findSurveysByEndAtLessThan(endDate);
  }

  @Override
  public List<Survey> findSurveysByTitleRegex(String titleRegex) {
    return surveyRepository.findSurveysByTitleLike(titleRegex);
  }

}
