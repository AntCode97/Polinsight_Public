package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

  @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
  @PutMapping("/survey")
  @Transactional
  public ApiUtils.ApiResult<Survey> surveyInfoUpdate(@RequestBody Survey survey) throws Exception {
    try {
      return success(surveyService.update(survey));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }


  @Async
  @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
  @GetMapping("/api/surveys/sync")
  public void surveySyncWithSurveyMonkey() throws InterruptedException {
    try {
      surveyService.getSurveyListAndSyncPerHour();
    } catch (InterruptedException e) {
      throw new InterruptedException(e.getMessage());
    }
  }

}
