package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {


  private final SurveyService surveyService;

  @Value("${custom.api.url}")
  private String baseURL;

  @GetMapping("/surveys")
  public ModelAndView getSurveyById(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    Long id = Long.parseLong(request.getParameter("surveyId"));
    mv.setViewName("");
    mv.addObject("survey", surveyService.findById(Survey.builder()
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
  public ResponseEntity<Map<String, Object>> getSurveyListFromSM(HttpSession session, String requestURL) throws MalformedURLException, URISyntaxException {
    Map<String, Object> map = new HashMap<>();
    List<Survey> surveys = null;
    //    session.setAttribute("survyes", );
    map.put("data", surveys);
    //    mv.setViewName("survey/surveylist");
    return ResponseEntity.ok(map);
  }

  @PostMapping("/survey/point/{survey_id}")
  public ModelAndView setPointToServey(@RequestParam("survey_id") int surveyId) {
    ModelAndView mv = new ModelAndView();
    List<Survey> surveys = null;
    mv.addObject("surveys", surveys);
    return mv;
  }

  @GetMapping("/surveys/sync")
  public ResponseEntity<Map<String, Object>> surveySyncWithSM() {
    Map<String, Object> map = new HashMap<>();
    try {
      surveyService.getSurveysWithSchedular();
      map.put("data", "sync success");
      map.put("error", null);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("data", null);
      map.put("error", e.getMessage());
    }
    return ResponseEntity.ok(map);
  }

}
