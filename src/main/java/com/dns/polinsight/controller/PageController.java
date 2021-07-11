package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PageController {

  private final PageService service;

  @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
  public ModelAndView home(@LoginUser SessionUser user) {
    ModelAndView mv = new ModelAndView();
    if (user != null) {
      mv.addObject("user", user);
    }
    mv.setViewName("index");
    return mv;
  }

  /*
   * 비밀번호 찾기 기능
   * */
  @GetMapping("/findpwd")
  public ModelAndView findpassword() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("findpwd");
    return mv;
  }

  @GetMapping("/login")
  public ModelAndView login() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("login");
    return mv;
  }

  @GetMapping("/join")
  public ModelAndView signUp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("basicsignup");
    return mv;
  }

  @GetMapping("/changepassword/{hash}")
  public ModelAndView changePassword(@PathVariable(name = "hash") String hash) {
    /*등록한 메일 주소로 전달한 페이지에서, 비밀번호 변경 클릭 시 리다이렉팅 될 주소*/
    // NOTE 2021-06-23 0023 : 해시가 우리 서버에서 발급한게 맞는지 확인한다
    ModelAndView mv = new ModelAndView();
    mv.setViewName("changepwd");
    return mv;
  }

  @GetMapping("/signup")
  public ModelAndView terms() {
    ModelAndView mv = new ModelAndView();
    try {
      mv.addObject("terms", service.getTerms());
      mv.setViewName("signupterms");
    } catch (IOException e) {
      mv.setViewName("5xx");
    }
    return mv;
  }

  @GetMapping("/panel")
  public ModelAndView panelSignUp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("panel");
    return mv;
  }

  @GetMapping("/denied")
  public ModelAndView deniedHandler() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("denied");
    return mv;
  }

}
