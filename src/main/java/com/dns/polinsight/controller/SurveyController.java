package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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


  /**
   * 서베이 몽키 수동 동기화
   */
  @GetMapping("/api/surveys/sync")
  public void surveySyncWithSurveyMonkey() {
    surveyService.getSurveyListAndSyncPerHour();
  }

}
