package com.dns.polinsight.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

  @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
  public ModelAndView home() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("index");
    return mv;
  }

  @RequestMapping(value = "/signup", method = {RequestMethod.POST, RequestMethod.GET})
  public ModelAndView signUp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("signup");
    return mv;
  }

}
