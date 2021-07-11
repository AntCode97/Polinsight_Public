package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService service;

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

  @PostMapping
  public ModelAndView saveSurvey(Survey survey) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("/surveyresult");
    return mv;
  }

}
