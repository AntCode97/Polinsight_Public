package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {


  private final SurveyService service;

  @Value("${custom.api.url}")
  private String baseURL;

  @GetMapping
  public ModelAndView getSurveyById(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    Long id = Long.parseLong(request.getParameter("surveyId"));
    mv.setViewName("");
    mv.addObject("survey", service.findById(Survey.builder()
                                                  .id(id)
                                                  .build()));

    return mv;

  }

  /*
   * 서베이 몽키로 리다이렉팅
   * */
  @GetMapping("/survey")
  public void redirectToSurvey(@LoginUser SessionUser sessionUser) {
    /*
     * 해시 발급 등 유저 맞춤 데이터 생성
     * */
  }

  /*
   * 서베이몽키에서 데이터를 파싱해 보여줌
   * 서베이 몽키에서 작성한 설문 리스트
   * */
  @GetMapping
  public ModelAndView getSurveyListFromSM(HttpSession session, String requestURL) throws MalformedURLException, URISyntaxException {
    ModelAndView mv = new ModelAndView();
    session.setAttribute("survyes", new RestTemplate().exchange(baseURL + requestURL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Survey>>() {}));
    mv.setViewName("survey/surveylist");
    return mv;
  }

  @PostMapping("/survey/point/{survey_id}")
  public ModelAndView setPointToServey(@RequestParam("survey_id") int surveyId) {
    ModelAndView mv = new ModelAndView();
    List<Survey> surveys = null;
    mv.addObject("surveys", surveys);
    return mv;
  }

}
