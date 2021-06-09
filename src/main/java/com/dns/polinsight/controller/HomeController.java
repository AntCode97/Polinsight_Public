package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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

    if (user != null) {
      model.addAttribute("userName", user.getName());
    }
    ModelAndView mv = new ModelAndView();
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
  public ModelAndView loginSuccess(HttpSession session) {
    ModelAndView mv = new ModelAndView();
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    session.setAttribute("user", new SessionUser(user));
    mv.setViewName("loginSuccess");
    return mv;
  }

}
