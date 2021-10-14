package com.dns.polinsight.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@PreAuthorize("hasAuthority('ADMIN, MANAGER')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

  @GetMapping("/memberlist")
  public ModelAndView adminMemberList() {
    return new ModelAndView("admin/admin_member_list");
  }


  @GetMapping("/surveylist")
  public ModelAndView adminSurveyList() {
    return new ModelAndView("admin/admin_survey_list");
  }


  @GetMapping
  public ModelAndView goAdminPage() {
    return new ModelAndView("admin/admin_dashboard");
  }


  @GetMapping("/boards/qna")
  public ModelAndView adminBoardQnA() {
    return new ModelAndView("admin_post_qna");
  }


  @GetMapping("/pointlist")
  public ModelAndView adminUserPointRequest() {
    return new ModelAndView("admin/admin_point_req_list");
  }


  @GetMapping("/qna")
  public ModelAndView qna() {
    return new ModelAndView("admin/admin_qna");
  }

}
