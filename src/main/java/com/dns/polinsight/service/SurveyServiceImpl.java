package com.dns.polinsight.service;

import com.dns.polinsight.domain.Collector;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.SurveyStatus;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.TooManyRequestException;
import com.dns.polinsight.mapper.SurveyMapping;
import com.dns.polinsight.repository.CollectorRepository;
import com.dns.polinsight.repository.SurveyRepository;
import com.dns.polinsight.types.CollectorStatusType;
import com.dns.polinsight.types.ProgressType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyRepository surveyRepository;

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
  public SurveyMapping findSurveyById(Long id) {
    return surveyRepository.findSurveyMappingById(id);
  }

  @Override
  public Page<SurveyMapping> findAll(Pageable pageable) {
    return surveyRepository.findAllSurveys(pageable);
  }

  @Override
  public Page<SurveyMapping> findAllByTypes(Pageable pageable, ProgressType type) {
    return surveyRepository.findAllByTypes(type, pageable);
  }

  @Override
  public Page<SurveyMapping> findAllSurveysByProgressTypeNotLike(Pageable pageable, ProgressType type) {
    return surveyRepository.findByProgressTypeNotLike(type, pageable);
  }

  @Override
  public Page<SurveyMapping> findAllAndRegex(Pageable pageable, String regex) {
    try {
      Long regexL = Long.parseLong(regex);
      return surveyRepository.findAllByRegex(regex, regexL, pageable);
    } catch (NumberFormatException e) {
      return surveyRepository.findAllByRegex(regex, -1L, pageable);
    }

  }

  @Override
  public Page<SurveyMapping> findAllByTypesAndRegex(Pageable pageable, ProgressType type, String regex) {
    try {
      Long regexL = Long.parseLong(regex);
      return surveyRepository.findAllByStatusProgressByRegex(type, regex, regexL, pageable);
    } catch (NumberFormatException e) {
      return surveyRepository.findAllByStatusProgressByRegex(type, regex, -1L, pageable);
    }


  }

  @Override
  public Page<Survey> findAll(Pageable pageable, String title) {
    return surveyRepository.findAllByTitleLikeOrderById(pageable, title);
  }

  @Override
  public Survey findById(long surveyId) {
    return surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundException::new);
  }

  @Override
  public Survey update(Survey survey) {
    return surveyRepository.save(survey);
  }

  @Override
  public Optional<Survey> findSurveyById(long surveyId) {
    return surveyRepository.findById(surveyId);
  }


  @Override
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void getSurveyListAndSyncPerHour() {
    final String additionalUrl = "/surveys?include=date_created,date_modified,preview";
    Set<Long> surveySet = new HashSet<>(surveyRepository.findAll().stream().parallel().map(Survey::getSurveyId).collect(Collectors.toSet()));
    try {
      ResponseEntity<Map> map = new RestTemplate().exchange(baseURL + additionalUrl, HttpMethod.GET, httpEntity, Map.class);
      List<Map<String, String>> tmplist = (List<Map<String, String>>) map.getBody().get("data");
      List<Survey> surveyList = tmplist.parallelStream()
                                       .filter(objMap -> !surveySet.contains(objMap.get("id")))
                                       .map(objmap -> Survey.builder()
                                                            .surveyId(Long.valueOf(objmap.get("id")))
                                                            .title(objmap.get("title"))
                                                            .createdAt(LocalDate.parse(objmap.get("date_created").split("T")[0]))
                                                            .endAt(LocalDate.parse(objmap.get("date_created").split("T")[0]))
                                                            .href(objmap.get("href")).point(0L)
                                                            .status(SurveyStatus.builder().count(0L).variables(new HashSet<>()).build())
                                                            .build())
                                       .filter(survey -> survey.getSurveyId() == 308896250)
                                       .map(survey -> this.getSurveyDetails(survey, httpEntity))
                                       .collect(Collectors.toList());
      log.info("Survey and Detail Info save success");

      surveyList = surveyRepository.saveAllAndFlush(surveyList);
      for (Survey survey : surveyList) {
        getCollectorBySurveyId(survey);
      }
    } catch (TooManyRequestException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new TooManyRequestException(e.getMessage());
    }
  }

  /**
   * CustomVariables, End Date 추가
   *
   * @return Custom Variable이 추가된 Survey 객체
   */
  private Survey getSurveyDetails(Survey survey, HttpEntity<Object> header) {
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "/surveys/" + survey.getSurveyId() + "/details", HttpMethod.GET, header, Map.class);
    Map<String, Object> map = res.getBody();
    Map<String, String> vars = (Map<String, String>) map.get("custom_variables");

    if (!(vars.keySet().contains("hash") && vars.keySet().contains("email"))) {
      survey = getSurveyCustomVariablesOrUpdate(survey);
    }
    survey.setQuestionCount(Long.valueOf(map.get("question_count") + "")); //--> 질문 갯수
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

    return collectorRepository.saveAllAndFlush(this.getParticipateUrl(collectors, survey));

  }

  private List<Collector> getParticipateUrl(List<Collector> collectorsList, Survey survey) {
    return collectorsList.parallelStream().map(collector -> {
      ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "collectors/" + collector.getCollectorId(), HttpMethod.GET, httpEntity, Map.class);
      Map<String, String> map = res.getBody();
      return Collector.builder()
                      .participateUrl(String.valueOf(map.get("url")))
                      .responseCount(Long.valueOf(String.valueOf(map.get("response_count"))))
                      .status(CollectorStatusType.valueOf(String.valueOf(map.get("status"))))
                      .collectorId(Long.parseLong(map.get("id")))
                      .survey(survey)
                      .build();
    }).collect(Collectors.toList());
  }

  @Override
  public void deleteSurveyById(long surveyId) {
    surveyRepository.deleteById(surveyId);
  }

  @Override
  public int adminSurveyUpdate(long id, long point, String create, String end, String progressType) {
    return surveyRepository.adminSurveyUpdate(id, point, create, end, progressType);
  }

  private Survey getSurveyCustomVariablesOrUpdate(Survey survey) {
    if (!survey.getStatus().getVariables().isEmpty()) {
      return survey;
    }
    String url = baseURL + "surveys/" + survey.getSurveyId();
    Map<String, String> custom_variables = new HashMap<>();
    custom_variables.put("email", "id");
    custom_variables.put("hash", "hash");
    Map<String, Object> map = new HashMap<>();
    map.put("custom_variables", custom_variables);
    // 응답으로 서베이 디테일이 날라옴

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<Map> entity = new HttpEntity<>(map, headers);
    ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    RestTemplate restTemplate = new RestTemplate(factory);
    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
    Map<String, Object> responseData = response.getBody();
    Map<String, String> var = (Map) responseData.get("custom_variables");
    survey.getStatus().setVariables(var.keySet());
    log.info("Survey {} variables updated, Title: {}", survey.getId(), survey.getTitle());
    return survey;
  }


}