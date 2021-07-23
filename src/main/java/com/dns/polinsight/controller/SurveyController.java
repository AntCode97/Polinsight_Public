package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

  private final UserService userService;

  @GetMapping("/surveys")
  public ModelAndView getSurveyById(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("");
    mv.addObject("survey", surveyService.findById(Survey.builder()
                                                        .id(Long.parseLong(request.getParameter("surveyId")))
                                                        .build()));
    return mv;
  }

  @GetMapping("/api/surveys")
  public ResponseEntity<Map<String, Object>> getAllSurveys() {
    Map<String, Object> map = new HashMap<>();

    try {
      map.put("data", surveyService.findAll());
      map.put("error", null);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("data", null);
      map.put("error", e.getMessage());
    }
    return ResponseEntity.ok(map);
  }

  /*
   * 서베이 몽키로 리다이렉팅
   * */
  @GetMapping("/survey/{surveyId}")
  public void redirectToSurvey(@LoginUser SessionUser sessionUser, @PathVariable(name = "surveyId") Long surveyId) {
    Survey userSelectedSurvey = surveyService.findById(Survey.builder().id(surveyId).build());
    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
    // 해시 발급 및 서베이 포인트와 결합하여 저장

  }

  @PostMapping("/survey/point/{survey_id}")
  public ModelAndView setPointToServey(@RequestParam("survey_id") int surveyId) {
    ModelAndView mv = new ModelAndView();
    List<Survey> surveys = null;
    mv.addObject("surveys", surveys);
    return mv;
  }


  /*
   * 서베이 수정
   * TODO: 2021/07/17
   * */
  @PutMapping("/survey")
  public ResponseEntity<Map<String, Object>> surveyInfoUpdate(Survey survey) {
    Map<String, Object> map = new HashMap<>();
    try {
      survey = surveyService.findById(survey);
      surveyService.update(survey);
      map.put("data", "");
      map.put("error", null);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("data", null);
      map.put("error", e.getMessage());
    }
    return ResponseEntity.ok(map);
  }

}
