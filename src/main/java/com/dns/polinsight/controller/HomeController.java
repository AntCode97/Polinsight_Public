package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

  private final HttpSession session;

  @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
  public ModelAndView home(Model model, @LoginUser SessionUser user) {
    ModelAndView mv = new ModelAndView();
    if (user != null) {
      mv.addObject("user", user);
    }
    mv.setViewName("index");
    return mv;
  }

  @RequestMapping(value = "/signup", method = {RequestMethod.GET})
  public ModelAndView signUp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("signup");
    return mv;
  }

  @RequestMapping(value = "/loginSuccess", method = {RequestMethod.GET})
  public ModelAndView loginSuccess(@LoginUser SessionUser user) {
    ModelAndView mv = new ModelAndView();
    session.setAttribute("user", user);
    mv.setViewName("loginSuccess");
    return mv;
  }

}
