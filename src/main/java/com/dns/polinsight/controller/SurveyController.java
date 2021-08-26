package com.dns.polinsight.controller;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.SurveyService;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

  private final ParticipateSurveyService participateSurveyService;

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
  @GetMapping("/api/survey")
  public ApiUtils.ApiResult<String> surveyClickEventHandler(@AuthenticationPrincipal User user,
                                                            @RequestParam("participate") String participateUrl,
                                                            @RequestParam("surveyId") long surveyId,
                                                            @Value("{custom.hash.pointsalt}") String salt) throws NoSuchAlgorithmException {
    if (user == null) {
      throw new BadCredentialsException("UnAuthorized");
    }
    try {
      Survey survey = surveyService.findSurveyBySurveyId(surveyId).get();
      log.info("{} participate survey that is : {}", user.getEmail(), survey.getTitle());
      List<String> someVariables = Arrays.asList(user.getEmail().toString(), survey.getSurveyId().toString());
      String hash = new HashUtil().makeHash(someVariables, salt);
      String sb = participateUrl + "?hash=" + hash + "&email=" + user.getEmail();
      participateSurveyService.saveParticipateSurvey(ParticipateSurvey.builder()
                                                                      .survey(survey)
                                                                      .hash(hash)
                                                                      .user(user)
                                                                      .participatedAt(LocalDateTime.now())
                                                                      .surveyPoint(survey.getPoint())
                                                                      .finished(false)
                                                                      .build());
      return success(sb);
    } catch (SurveyNotFoundException e) {
      throw new SurveyNotFoundException(e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      throw new NoSuchAlgorithmException(e.getMessage());
    }
  }

}
