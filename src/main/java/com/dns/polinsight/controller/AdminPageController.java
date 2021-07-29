package com.dns.polinsight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

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

}
