package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

  private final UserService userService;

  private final ParticipateSurveyService participateSurveyService;

  //  @Deprecated
  //  @GetMapping("/surveys")
  //  public ModelAndView getSurveyById(HttpServletRequest request) {
  //    ModelAndView mv = new ModelAndView();
  //    mv.setViewName("");
  //    mv.addObject("survey", surveyService.findById(Long.parseLong(request.getParameter("surveyId"))));
  //    return mv;
  //  }

  /*
   * 서베이 몽키로 리다이렉팅
   * */
  //  @GetMapping("/survey/{surveyId}")
  //  public void redirectToSurvey(@AuthenticationPrincipal User user, @PathVariable(name = "surveyId") Long surveyId) {
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

  @PutMapping("/survey")
  public ApiUtils.ApiResult<Survey> surveyInfoUpdate(@RequestBody Survey survey) throws Exception {
    try {
      return success(surveyService.update(survey));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 로그인한 사용자가 서베이 클릭시
   * */
  @GetMapping("/api/survey/{surveyId}")
  public ApiUtils.ApiResult<String> surveyClickEventHandler(@AuthenticationPrincipal User user,
                                                            @PathVariable("surveyId") long surveyId,
                                                            @Value("{custom.hash.pointsalt}") String salt) throws NoSuchAlgorithmException {
    if (user == null) {
      throw new BadCredentialsException("UnAuthorized");
    }
    try {
      Survey survey = surveyService.findSurveyBySurveyId(surveyId).orElseThrow(SurveyNotFoundException::new);
      log.info("user click survey info is : {}", survey.toString());
      List<String> someVariables = Arrays.asList(user.getEmail().toString(), survey.getSurveyId().toString());
      String sb = survey.getHref() +
          "?hash=" + new HashUtil().makeHash(someVariables, salt) +
          "&name=" + user.getEmail();
      return success(sb);
    } catch (SurveyNotFoundException e) {
      throw new SurveyNotFoundException(e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      throw new NoSuchAlgorithmException(e.getMessage());
    }
  }

}
