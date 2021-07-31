package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

  private final UserService userService;

  private final ParticipateSurveyService participateSurveyService;

  @GetMapping("/surveys")
  public ModelAndView getSurveyById(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("");
    mv.addObject("survey", surveyService.findById(Survey.builder()
                                                        .id(Long.parseLong(request.getParameter("surveyId")))
                                                        .build()));
    return mv;
  }

  /*
   * 서베이 몽키로 리다이렉팅
   * */
  //  @GetMapping("/survey/{surveyId}")
  //  public void redirectToSurvey(@LoginUser SessionUser sessionUser, @PathVariable(name = "surveyId") Long surveyId) {
  //    Survey userSelectedSurvey = surveyService.findById(Survey.builder().id(surveyId).build());
  //    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
  //    // 해시 발급 및 서베이 포인트와 결합하여 저장
  //    participateSurveyService.saveAndUpdate(null);
  //  }

  /*
   * 서베이의 포인트 적립을 위한 메서드
   * */
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

  /*
   * 로그인한 사용자가 서베이 클릭시
   * */
  @PostMapping("/survey/{surveyId}")
  public ModelAndView surveyClickEventHandler(@LoginUser SessionUser sessionUser,
                                              @PathVariable("surveyId") long surveyId,
                                              @Value("{custom.hash.pointsalt}") String salt) throws NoSuchAlgorithmException {
    //    ModelAndView mv = new ModelAndView();
    if (sessionUser == null) {
      throw new BadCredentialsException("UnAuthorized");
    }
    System.out.println("surveyId: " + surveyId);
    try {
      // TODO: 2021/07/27 : 값 넣기
      List<String> someVariables = new ArrayList<>();
      Survey survey = surveyService.findSurveyById(surveyId).orElseThrow(SurveyNotFoundException::new);
      log.info("user click survey info is : {}", survey.toString());
      //      String sb = "redirect:" +
      //          survey.getHref() +
      //          "?hash=" + new HashUtil().makeHash(someVariables, salt) +
      //          "&name=" + sessionUser.getEmail();
      //      mv.setViewName(sb);
      return new ModelAndView("redirect:https://www.naver.com");
    } catch (SurveyNotFoundException e) {
      throw new SurveyNotFoundException(e.getMessage());
    }
    //    } catch (NoSuchAlgorithmException e) {
    //      throw new NoSuchAlgorithmException(e.getMessage());
    //    }
  }

}
