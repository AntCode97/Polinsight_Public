package com.dns.polinsight.service;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.SurveyStatus;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.SurveyMonkeyDTO;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.TooManyRequestException;
import com.dns.polinsight.repository.SurveyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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

  @Override
  //  @Scheduled(cron = "0 0 0/1 * * *")
  public List<Survey> getSurveyListAndSyncPerHour() {
    final String additionalUrl = "/surveys?include=date_created,date_modified,preview";

    try {
      ResponseEntity<Map> map = new RestTemplate().exchange(baseURL + additionalUrl, HttpMethod.GET, httpEntity, Map.class);
      List<Map<String, String>> tmplist = (List<Map<String, String>>) map.getBody().get("data");
      List<Survey> surveyList = tmplist.stream().map(objmap -> SurveyMonkeyDTO.builder()
                                                                              .id(Long.valueOf(objmap.get("id")))
                                                                              .title(objmap.get("title"))
                                                                              .createdAt(LocalDateTime.parse(objmap.get("date_created")))
                                                                              .href(objmap.get("href"))
                                                                              .build()).map(SurveyMonkeyDTO::toSurvey).collect(Collectors.toList());

      surveyList = surveyList.parallelStream().map(survey -> {
        survey.setStatus(new SurveyStatus());
        try {
          return this.getSurveyDetails(survey, httpEntity);
        } catch (JsonProcessingException e) {
          log.info(e.getMessage());
        }
        return survey;
      }).collect(Collectors.toList());
      log.info("survey sync success");
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


  private Survey getSurveyDetails(Survey basicSurvey, HttpEntity<Object> header) throws JsonProcessingException {
    String apiUrl = "/surveys/" + basicSurvey.getSurveyId() + "/details";
    ResponseEntity<SurveyMonkeyDTO> res = new RestTemplate().exchange(baseURL + apiUrl, HttpMethod.GET, header, SurveyMonkeyDTO.class);
    SurveyMonkeyDTO tmpDto = res.getBody();
    LocalDateTime createAt = LocalDateTime.parse(tmpDto.getDate_created());
    basicSurvey.getStatus().setVariables(tmpDto.getCustom_variables().keySet());
    Calendar calendar = Calendar.getInstance();
    basicSurvey.setCreatedAt(createAt);
    basicSurvey.setEndAt(createAt.plusMonths(6));
    return basicSurvey;
  }

  /**
   * @param url
   *     : survey/{surveyId}/collectors : 서베이에 걸린 collector 리스트 가져옴옴
   */
  private void getCollectorBySurveyId(String url, HttpEntity<Object> header) {
    ResponseEntity<Map> res = new RestTemplate().exchange(url, HttpMethod.GET, header, Map.class);
    Map dto = res.getBody();
    // 설문에 걸린 모든 collector 객체 (name, id, href)
    List<Map<String, String>> list = (List<Map<String, String>>) dto.get("data");
    List<Map> mapList = list.stream().map(map -> {
      return this.getCollectorsDetailByCollectorId(map.get("href"), header);
    }).collect(Collectors.toList());
  }

  private Map getCollectorsDetailByCollectorId(String url, HttpEntity<Object> header) {
    ResponseEntity<Map> res = new RestTemplate().exchange(url, HttpMethod.GET, header, Map.class);
    Map dto = res.getBody();
    String collectorId = (String) dto.get("id");
    String surveyId = (String) dto.get("survey_id");
    String surveyTitle = (String) dto.get("name");
    long surveyResponseCount = (long) dto.get("response_count");
    String surveyParticipateUrl = (String) dto.get("url"); // 여기에 해시와 여러 정보를 넘겨 설문참여하도록
    String href = (String) dto.get("href");
    return new HashMap();
  }

  private long getTotalResponse(Long surveyId, HttpEntity<Object> header) {
    String api = "/surveys/" + surveyId + "/responses/bulk";
    ResponseEntity<SurveyMonkeyDTO> res = new RestTemplate().exchange(baseURL + api, HttpMethod.GET, header, SurveyMonkeyDTO.class);
    return res.getBody().getTotal();
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

}
