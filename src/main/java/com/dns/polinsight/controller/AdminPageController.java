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

  @GetMapping
  public ModelAndView goAdminPage() {
    return new ModelAndView("admin/admin");
  }


  /*
   * 게시글 관리 페이지
   * */
  @GetMapping("/board/manage")
  public ModelAndView getBoardManage() {
    return new ModelAndView("admin/admin_board_list");
  }

  /*
   * 공지사항 관리 페이지
   * */
  @GetMapping("/noti/manage")
  public ModelAndView getNotiManage() {
    return new ModelAndView("admin/admin_board_register");
  }

  @GetMapping("/user/manage")
  public ModelAndView getUserManage() {
    return new ModelAndView("admin/admin_board_view");
  }


  @GetMapping("/boards/notice")
  public ModelAndView adminBoardnotice() {
    return new ModelAndView("admin/");
  }

  @GetMapping("/boards/download")
  public ModelAndView adminBoarddownload() {
    return new ModelAndView("admin/");
  }

  @GetMapping("/boards/QnA")
  public ModelAndView adminBoardQnA() {
    return new ModelAndView("admin/");
  }

}
