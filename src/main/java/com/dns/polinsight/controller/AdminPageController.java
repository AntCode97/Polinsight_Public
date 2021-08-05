package com.dns.polinsight.controller;

import com.dns.polinsight.service.SurveyService;
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


//  /*
//   * 게시글 관리 페이지
//   * */
//  @GetMapping("/post/manage")
//  public ModelAndView getBoardManage() {
//    return new ModelAndView("admin/admin_board_list");
//  }
//
//  /*
//   * 공지사항 관리 페이지
//   * */
//  @GetMapping("/noti/manage")
//  public ModelAndView getNotiManage() {
//    return new ModelAndView("admin/admin_board_register");
//  }
//
//  @GetMapping("/user/manage")
//  public ModelAndView getUserManage() {
//    return new ModelAndView("admin/admin_board_view");
//  }
//
  //
  //  @GetMapping("/posts/notice")
  //  public ModelAndView adminBoardnotice() {
  //    return new ModelAndView("admin/");
  //  }

    @GetMapping("/boards/download")
    public ModelAndView adminBoarddownload() {
      return new ModelAndView("admin_post_download");
    }

    @GetMapping("/boards/qna")
    public ModelAndView adminBoardQnA() {
      return new ModelAndView("admin_post_qna");
    }

  @GetMapping("/survey/{id}")
  public ModelAndView adminGetSurveyDetailInfo(@PathVariable("id") long surveyId) {
    log.info("surveyid: " + surveyId);
    ModelAndView mv = new ModelAndView("redirect:admin/admin_survey_info");
    mv.addObject("survey", surveyService.findSurveyById(surveyId));
    return mv;
  }

  @GetMapping("/surveyinfo")
  public ModelAndView adminGetDetailPage(ModelAndView mav) {
    mav.setViewName("admin/admin_survey_info");
    return mav;
  }

  @GetMapping("/pointlist")
  public ModelAndView adminUserPointRequest() {
    return new ModelAndView("admin/admin_point_req_list");
  }

  //  @GetMapping("/pointreqinfo")
  //  public ModelAndView adminUserPointRequestInfo(ModelAndView mav) {
  //    mav.setViewName("admin/admin_survey_info");
  //    return mav;
  //  }

}
