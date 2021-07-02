package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

  @GetMapping("/findpassword")
  public ModelAndView findpassword() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("findpwd");
    return mv;
  }

  @GetMapping("/loginpage")
  public ModelAndView login() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("login");
    return mv;
  }

  @GetMapping("/signup")
  public ModelAndView signUp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("basicsignup");
    return mv;
  }

  @GetMapping("/changepassword/{hash}")
  public ModelAndView changePassword(@PathVariable(name = "hash") String hash) {
    // NOTE 2021-06-23 0023 : 해시가 우리 서버에서 발급한게 맞는지 확인한다
    ModelAndView mv = new ModelAndView();
    mv.setViewName("changepwd");
    return mv;
  }

  @GetMapping("/terms")
  public ModelAndView terms() {
    ModelAndView mv = new ModelAndView();
    try {
      mv.addObject("terms", service.getTerms());
      mv.setViewName("terms");
    } catch (IOException e) {
      mv.setViewName("5xx");
    }
    return mv;
  }

}
