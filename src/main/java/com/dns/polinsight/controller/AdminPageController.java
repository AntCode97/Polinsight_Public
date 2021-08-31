package com.dns.polinsight.controller;

import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

  private final SurveyService surveyService;

  private final UserService userService;

  @GetMapping("/memberlist")
  public ModelAndView adminMemberList() {
    return new ModelAndView("admin/admin_member_list");
  }

  @GetMapping("/memberinfo")
  public ModelAndView adminMemberDetailInfo() {
    return new ModelAndView("admin/admin_member_info");
  }

  @GetMapping("/surveylist")
  public ModelAndView adminSurveyList() {
    return new ModelAndView("admin/admin_survey_list");
  }

  @GetMapping
  public ModelAndView goAdminPage() {
    return new ModelAndView("admin/admin_dashboard");
  }

  @GetMapping("/boards/download")
  public ModelAndView adminBoarddownload() {
    return new ModelAndView("admin_post_download");
  }

  @GetMapping("/boards/qna")
  public ModelAndView adminBoardQnA() {
    return new ModelAndView("admin_post_qna");
  }

  @GetMapping("/survey/{id}")
  public ModelAndView adminGetSurveyDetailInfo(@PathVariable("id") long id) {
    ModelAndView mv = new ModelAndView("redirect:admin/admin_survey_info");
    mv.addObject("survey", surveyService.findSurveyById(id).orElseThrow(SurveyNotFoundException::new));
    return mv;
  }

  @Deprecated
  @GetMapping("/surveyinfo/{id}")
  public ModelAndView adminGetSurveyDetailPage(@PathVariable("id") long id) {
    ModelAndView mv = new ModelAndView("admin/admin_survey_info");
    mv.addObject("survey", surveyService.findSurveyById(id).orElseThrow(SurveyNotFoundException::new));
    return mv;
  }

  @GetMapping("/memberinfo/{id}")
  public ModelAndView adminGetMemberDetailPage(@PathVariable("id") long memberId) {
    ModelAndView mv = new ModelAndView("admin/admin_member_info");
    mv.addObject("user", new UserDto(userService.findById(memberId).orElseThrow(SurveyNotFoundException::new)));
    return mv;
  }

  @GetMapping("/pointlist")
  public ModelAndView adminUserPointRequest() {
    return new ModelAndView("admin/admin_point_req_list");
  }

  @GetMapping("/events")
  public ModelAndView events() {
    return new ModelAndView("admin/admin_events");
  }

  @GetMapping("/qna")
  public ModelAndView qna() {
    return new ModelAndView("admin/admin_qna");
  }

  @GetMapping("/faq")
  public ModelAndView faq() {
    return new ModelAndView("admin/admin_faq");
  }

}
