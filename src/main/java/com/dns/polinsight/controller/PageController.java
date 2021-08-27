package com.dns.polinsight.controller;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  public ModelAndView home(@AuthenticationPrincipal User user) {
    ModelAndView mv = new ModelAndView();
    if (user != null) {
      mv.addObject("user", new UserDto(user));
    } else
      mv.clear();
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

  @GetMapping("/success_basic")
  public ModelAndView successBasicMemberSignUp() {
    return new ModelAndView("member/success_basicmember");
  }

  @GetMapping("/success_panel")
  public ModelAndView successPanelMemberSignUp() {
    return new ModelAndView("member/success_panel");
  }

  @GetMapping("/basictopanel")
  public ModelAndView changeBasicToPanel(@AuthenticationPrincipal User user, HttpSession session) {
    session.setAttribute("basic_user", user);
    return new ModelAndView("redirect:/panel");
  }

  @GetMapping("/find")
  public ModelAndView find() {
    return new ModelAndView("member/find");
  }

  @GetMapping("/events")
  public ModelAndView events() {
    return new ModelAndView("posts/events");
  }

  @GetMapping("/qna")
  public ModelAndView qna() {
    return new ModelAndView("posts/qna");
  }

  @GetMapping("/faq")
  public ModelAndView faq() {
    return new ModelAndView("posts/faq");
  }

  @GetMapping("/research/online")
  public String getResearchOnline(Model model) {
    model.addAttribute("checked", "online");
    return "research/onlineSurvey";
  }

  @GetMapping("/research/pols")
  public String getResearchPols(Model model) {
    model.addAttribute("checked", "pols");
    return "research/pols";
  }

  @GetMapping("/company/introduce")
  public String getCompanyIntroduce(Model model) {
    model.addAttribute("checked", "intro");
    return "company/introduce";
  }

  @GetMapping("/company/map")
  public String getCompanyMap(Model model) {
    model.addAttribute("checked", "map");
    return "company/map";
  }

  @GetMapping("/business/category")
  public String getBusinessCategory(Model model) {
    model.addAttribute("checked", "category");
    return "business/category";
  }

  @GetMapping("/business/result")
  public String getBusinessResult(Model model) {
    model.addAttribute("checked", "result");
    return "business/result";
  }

}
