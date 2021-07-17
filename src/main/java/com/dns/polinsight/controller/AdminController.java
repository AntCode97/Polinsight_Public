package com.dns.polinsight.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  /*
   * 관리자 마이페이지
   * */
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

}
