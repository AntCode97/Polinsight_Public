package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PageController {

  @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
  public ModelAndView home(@LoginUser SessionUser user) {
    ModelAndView mv = new ModelAndView();
    if (user != null) {
      mv.addObject("user", user);
    }
    mv.setViewName("index");
    return mv;
  }

  @GetMapping("/findpwd")
  public ModelAndView findpassword() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("member/findpwd");
    return mv;
  }

  @GetMapping("/login")
  public ModelAndView login() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("member/login");
    return mv;
  }

  @GetMapping("/join")
  public ModelAndView signUp() {
    return new ModelAndView("member/basicsignup");
  }


  @GetMapping("/signup")
  public ModelAndView contract() {
    return new ModelAndView("member/contract");
  }

  @GetMapping("/panel")
  public ModelAndView panelSignUp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("member/panel");
    return mv;
  }

  @GetMapping("/denied")
  public ModelAndView deniedHandler() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("error/denied");
    return mv;
  }

  @GetMapping("/panelagreement")
  public ModelAndView panelAgreement() {
    return new ModelAndView("member/pagree");
  }

  @GetMapping("/success_basic")
  public ModelAndView successBasicMemberSignUp() {
    return new ModelAndView("member/success_basicmember");
  }

  @GetMapping("/success_panel")
  public ModelAndView successPanelMemberSignUp() {
    return new ModelAndView("member/success_panel");
  }

  @GetMapping("/basictopanel")
  public ModelAndView changeBasicToPanel(@LoginUser SessionUser sessionUser, HttpSession session) {
    session.invalidate();
    session.setAttribute("basic_user", sessionUser);
    return new ModelAndView("redirect:/panelagreement");
  }

  @GetMapping("/find")
  public ModelAndView find() {
    return new ModelAndView("member/find");
  }

  @GetMapping("/test")
  public ModelAndView testFunction() {
    return new ModelAndView("test");
  }

}
