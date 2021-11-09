package com.dns.polinsight.service;

import com.dns.polinsight.domain.Collector;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.SurveyStatus;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.TooManyRequestException;
import com.dns.polinsight.projection.SurveyMapping;
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
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyRepository surveyRepository;

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
  public Optional<Survey> findById(Long id) {
    return surveyRepository.findById(id);
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
  public void deleteSurveyById(long surveyId) {
    surveyRepository.deleteById(surveyId);
  }

  @Override
  public int adminSurveyUpdate(long id, long point, String create, String end, String progressType) {
    return surveyRepository.adminSurveyUpdate(id, point, create, end, progressType);
  }

  @Override
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void getSurveyListAndSyncPerHour() {
    log.info("Sync Survey save start At {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    // https://developer.surveymonkey.com/api/v3/#api-endpoints-get-surveys
    final String additionalUrl = "/surveys?include=date_created,date_modified,preview";
    Set<Long> surveySet = surveyRepository.findAll().parallelStream().map(Survey::getSurveyId).collect(Collectors.toUnmodifiableSet());
    try {
      ResponseEntity<Map> map = new RestTemplate().exchange(baseURL + additionalUrl, HttpMethod.GET, httpEntity, Map.class);
      List<Map<String, String>> tmplist = (List<Map<String, String>>) map.getBody().get("data");
      tmplist.stream()
             .filter(objMap -> !surveySet.contains(Long.parseLong(objMap.get("id"))))
             .map(objMap -> Survey.builder()
                                  .surveyId(Long.valueOf(objMap.get("id")))
                                  .title(objMap.get("title"))
                                  .createdAt(LocalDate.parse(objMap.get("date_created").split("T")[0]))
                                  .endAt(LocalDate.parse(objMap.get("date_created").split("T")[0]))
                                  .href(objMap.get("href")).point(0L)
                                  .status(SurveyStatus.builder().count(0L).variables(new HashSet<>()).build())
                                  .build())
             .map(survey -> {
               try {
                 log.info("survey : {} / {} will update", survey.getTitle(), survey.getSurveyId());
                 Thread.sleep(500);
                 Survey tmp = this.getSurveyDetails(survey, httpEntity);
                 log.info("survey : {} / {} has updated", survey.getTitle(), survey.getSurveyId());
                 return tmp;
               } catch (InterruptedException e) {
                 log.error(e.getMessage());
               }
               return null;
             })
             .filter(Objects::nonNull)
             .map(survey -> {
               try {
                 return getCollectorBySurveyId(survey);
               } catch (Exception e) {
                 log.error(e.getMessage());
               }
               return null;
             })
             .filter(Objects::nonNull)
             .forEach(surveyRepository::save);

      log.info("Success to save survey and detail info");

      log.info("Survey sync and Save Success At {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    } catch (TooManyRequestException e) {
      log.error(e.getMessage());
    }
  }

  private Survey getSurveyDetails(Survey survey, HttpEntity<Object> header) throws InterruptedException {
    Thread.sleep(1000);
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "/surveys/" + survey.getSurveyId() + "/details", HttpMethod.GET, header, Map.class);
    Map<String, Object> map = res.getBody();
    Map<String, String> vars = (Map<String, String>) map.get("custom_variables");
    if (!vars.containsKey("hash") || !vars.containsKey("email")) {
      survey = updateSurveyCustomVariablesOrUpdate(survey);
    }
    survey.setQuestionCount(Long.valueOf(map.get("question_count") + ""));
    return survey;
  }

  private Survey updateSurveyCustomVariablesOrUpdate(Survey survey) throws InterruptedException {
    if (!survey.getStatus().getVariables().isEmpty()) {
      return survey;
    }
    Thread.sleep(500);
    Map<String, String> custom_variables = new HashMap<>();
    custom_variables.put("email", "user_id");
    custom_variables.put("hash", "hash");
    Map<String, Object> map = new HashMap<>();
    map.put("custom_variables", custom_variables);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<Map> entity = new HttpEntity<>(map, headers);
    ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    RestTemplate restTemplate = new RestTemplate(factory);
    ResponseEntity<Map> response = restTemplate.exchange(baseURL + "surveys/" + survey.getSurveyId(), HttpMethod.PATCH, entity, Map.class);
    Map<String, Object> responseData = response.getBody();
    Map<String, String> var = (Map) responseData.get("custom_variables");
    survey.getStatus().setVariables(var.keySet());
    log.info("Survey {} variables updated, Title: {}", survey.getId(), survey.getTitle());
    return survey;
  }

  private Survey getCollectorBySurveyId(Survey survey) throws InterruptedException {
    Thread.sleep(1000);
    ResponseEntity<Map> res = new RestTemplate().exchange(baseURL + "surveys/" + survey.getSurveyId() + "/collectors", HttpMethod.GET, httpEntity, Map.class);
    List<Map<String, String>> mapList = (List<Map<String, String>>) res.getBody().get("data");
    List<Collector> collectors = mapList.parallelStream()
                                        .map(obj -> Collector.builder()
                                                             .collectorId(Long.parseLong(obj.get("id")))
                                                             .name(obj.get("name"))
                                                             .href(obj.get("href"))
                                                             .survey(survey)
                                                             .build())
                                        .map(collector -> {
                                          // get participate url
                                          ResponseEntity<Map> collectorResponse = new RestTemplate().exchange(baseURL + "collectors/" + collector.getCollectorId(), HttpMethod.GET, httpEntity, Map.class);
                                          Map<String, String> map = collectorResponse.getBody();
                                          return Collector.builder()
                                                          .participateUrl(String.valueOf(map.get("url")))
                                                          .responseCount(Long.valueOf(String.valueOf(map.get("response_count"))))
                                                          .status(CollectorStatusType.valueOf(String.valueOf(map.get("status"))))
                                                          .collectorId(Long.parseLong(map.get("id")))
                                                          .survey(survey)
                                                          .build();
                                        })
                                        .collect(Collectors.toList());
    if (collectors.size() > 0)
      survey.setCollector(collectors.get(0));
    return survey;
  }


}