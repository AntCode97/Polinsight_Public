package com.dns.polinsight.service;

import com.dns.polinsight.domain.Collector;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.SurveyStatus;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.SurveyDto;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.TooManyRequestException;
import com.dns.polinsight.repository.CollectorRepository;
import com.dns.polinsight.repository.SurveyJdbcTemplate;
import com.dns.polinsight.repository.SurveyRepository;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyRepository surveyRepository;

  private final SurveyJdbcTemplate surveyJdbcTemplate;

  private final CollectorRepository collectorRepository;

  @Value("${custom.api.accessToken}")
  private String accessToken;

  private HttpEntity<Object> httpEntity;

  @Value("${custom.api.url}")
  private String baseURL;

  @PostConstruct
  public void init() {
    HttpHeaders header = new HttpHeaders();
    header.setBearerAuth(accessToken);
    httpEntity = new HttpEntity<>(header);
  }

  @Override
  public Survey save(Survey survey) {
    return surveyRepository.save(survey);
  }

  @Override
  public List<Survey> findAll() {
    return surveyRepository.findAll();
  }

  @Override
  public Page<Survey> findAll(Pageable pageable) {
    return surveyRepository.findAll(pageable);
  }

  @Override
  public Survey findById(long surveyId) {
    return surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundException::new);
  }

  @Override
  public Survey update(Survey survey) {
    return surveyRepository.save(survey);
  }

  // TODO: 2021-08-13 테스트 필요 --> 테스트 시 테스트용 서버 만들어서 사용할 것
  @Override
  @Transactional
  //  @Scheduled(cron = "0 0 0/1 * * *")
  public List<Survey> getSurveyListAndSyncPerHour() {
    final String additionalUrl = "/surveys?include=date_created,date_modified,preview";
    Set<Long> surveySet = new HashSet<>(surveyRepository.findAll().stream().parallel().map(Survey::getSurveyId).collect(Collectors.toSet()));
    try {
      ResponseEntity<Map> map = new RestTemplate().exchange(baseURL + additionalUrl, HttpMethod.GET, httpEntity, Map.class);
      List<Map<String, String>> tmplist = (List<Map<String, String>>) map.getBody().get("data");
      List<Survey> surveyList = tmplist.parallelStream().filter(objMap -> !surveySet.contains(objMap.get("id")))
                                       .map(objmap -> Survey.builder()
                                                            .surveyId(Long.valueOf(objmap.get("id")))
                                                            .title(objmap.get("title"))
                                                            .createdAt(LocalDate.parse(objmap.get("date_created")))
                                                            .href(objmap.get("href"))
                                                            .status(SurveyStatus.builder().variables(new HashSet<>()).build())
                                                            .build())
                                       .map(survey -> this.getSurveyDetails(survey, httpEntity))
                                       .collect(Collectors.toList());
      log.info("Survey and Detail Info save success");
      surveyRepository.saveAllAndFlush(surveyList);
      surveyList.parallelStream().map(this::getCollectorBySurveyId);
      return surveyRepository.saveAllAndFlush(surveyList);
    } catch (TooManyRequestException e) {
      log.error(e.getMessage());
      throw new TooManyRequestException(e.getMessage());
    }
  }

  @Override
  public Set<Survey> getUserParticipateSurvey(User user) {
    Set<Survey> ret = new HashSet<>();
    for (var surveyId : user.getParticipateSurvey())
      ret.add(this.findById(surveyId));
    return ret;
  }

  /**
   * CustomVariables, End Date 추가
   *
   * @return Custom Variable이 추가된 Survey 객체
   */
  private Survey getSurveyDetails(Survey survey, HttpEntity<Object> header) {
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "/surveys/" + survey.getSurveyId() + "/details", HttpMethod.GET, header, Map.class);
    Map<String, Object> map = res.getBody();
    survey.getStatus().setVariables(((Map<String, String>) map.get("custom_variables")).keySet());
    survey.setQuestionCount((Long) map.get("question_count"));
    return survey;
  }

  public List<Collector> getCollectorBySurveyId(Survey survey) {
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "surveys/" + survey.getSurveyId() + "/collectors", HttpMethod.GET, httpEntity, Map.class);
    List<Map<String, String>> mapList = (List<Map<String, String>>) res.getBody().get("data");
    List<Collector> collectors = mapList.parallelStream().map(obj -> Collector.builder()
                                                                              .collectorId(Long.parseLong(obj.get("id")))
                                                                              .name(obj.get("name"))
                                                                              .href(obj.get("href"))
                                                                              .survey(survey)
                                                                              .build())
                                        .collect(Collectors.toList());
    return collectorRepository.saveAllAndFlush(this.getParticipateUrl(collectors));
  }

  public List<Collector> getParticipateUrl(List<Collector> collectorsList) {
    return collectorsList.parallelStream().map(collector -> {
      ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "collectors/" + collector.getCollectorId(), HttpMethod.GET, httpEntity, Map.class);
      Map<String, String> map = res.getBody();
      return Collector.builder()
                      .participateUrl(String.valueOf(map.get("url")))
                      .responseCount(Long.valueOf(map.get("response_count")))
                      .status(CollectorStatusType.valueOf(String.valueOf(map.get("status"))))
                      .build();
    }).collect(Collectors.toList());
  }

  @Override
  public void deleteSurveyById(long surveyId) {
    surveyRepository.deleteById(surveyId);
  }

  @Override
  public List<Survey> findSurveysByEndDate(LocalDateTime endDate) {
    return surveyRepository.findSurveysByEndAtLessThan(endDate);
  }

  @Override
  public List<Survey> findSurveysByTitleRegex(String titleRegex, Pageable pageable) {
    return surveyRepository.findSurveysByTitleLike(titleRegex, pageable);
  }

  @Override
  public Optional<Survey> findSurveyById(long surveyId) {
    return surveyRepository.findById(surveyId);
  }

  @Override
  public Optional<Survey> findSurveyBySurveyId(long surveyId) {
    return surveyRepository.findSurveyBySurveyId(surveyId);
  }

  @Override
  public long countAllSurvey() {
    return surveyRepository.count();
  }

  @Override
  public void adminSurveyUpdate(long id, long point, String create, String end, String progressType) {
    surveyRepository.adminSurveyUpdate(id, point, create, end, progressType);
  }

  @Override
  public List<SurveyDto> findAllSurveyWithCollector(Pageable pageable) {
    // TODO: 2021-08-17 : 쿼리 마무리
    List<SurveyDto> list = surveyJdbcTemplate.findAllSurveyWithCollector("progress");
    list.addAll(surveyJdbcTemplate.findAllSurveyWithCollector("endAt"));
    Collections.sort(list);
    return list;
  }

  /**
   * @param survey
   *     : CustomVariables가 등록되지 않은 서베이 엔티티
   */
  private void getSurveyCustomVariablesOrUpdate(Survey survey) {
    if (!survey.getStatus().getVariables().isEmpty()) {
      return;
    }
    httpEntity.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(httpEntity.getHeaders());
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "", HttpMethod.PATCH, entity, Map.class);

    ResponseEntity<Map> response = new RestTemplate().exchange(baseURL + "surveys/" + survey.getSurveyId(), HttpMethod.GET, httpEntity, Map.class);
  }

}
