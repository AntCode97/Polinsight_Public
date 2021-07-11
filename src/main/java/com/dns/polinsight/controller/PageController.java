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
    mv.setViewName("signup");
    return mv;
  }

  @GetMapping("/changepassword")
  public ModelAndView changePassword() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("changepwd");
    return mv;
  }

}
