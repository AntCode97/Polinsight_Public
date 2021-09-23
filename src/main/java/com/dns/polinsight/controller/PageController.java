package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.UnAuthorizedException;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PageController {

  @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
  public ModelAndView home(@CurrentUser User user) {
    ModelAndView mv = new ModelAndView();
    if (user != null) {
      mv.addObject("user", new UserDto(user));
    } else
      mv.clear();
    mv.setViewName("index");
    return mv;
  }

  @GetMapping("/login")
  public ModelAndView login(@CurrentUser User user) {
    if (user == null) {
      return new ModelAndView("member/login");
    } else {
      return new ModelAndView("redirect:/");
    }
  }

  @GetMapping("/join")
  public ModelAndView signUp(@CurrentUser User user) {
    if (user == null) {
      return new ModelAndView("member/join");
    } else {
      return new ModelAndView("redirect:/");
    }

  }


  @GetMapping("/signup")
  public ModelAndView contract(@CurrentUser User user) {
    if (user == null) {
      return new ModelAndView("member/contract");
    } else {
      return new ModelAndView("redirect:/");
    }

  }

  @GetMapping("/panel")
  public ModelAndView panelSignUp(@CurrentUser User user) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("_panel");
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
    return new ModelAndView("member/success_basic");
  }

  @GetMapping("/success_panel")
  public ModelAndView successPanelMemberSignUp() {
    return new ModelAndView("member/success_panel");
  }

  @GetMapping("/basictopanel")
  public ModelAndView changeBasicToPanel(@CurrentUser User user, HttpSession session) {
    session.setAttribute("basic_user", user);
    return new ModelAndView("redirect:/panel");
  }

  @GetMapping("/find")
  public ModelAndView find() {
    return new ModelAndView("member/find_info");
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

  @PermitAll
  @GetMapping("/test")
  public ModelAndView testPage() {
    return new ModelAndView("signup");
  }

  @GetMapping("/accumulate_error")
  public ModelAndView pointAccumulateError() {
    return new ModelAndView("redirect:/point_accumulate_error");
  }

  @GetMapping("/point_accumulate_error")
  public ModelAndView pointAccumulateErrorPage() {
    return new ModelAndView("error/point_accumulate_error");
  }

  @GetMapping("/upgradepanel")
  public ModelAndView chageNormalUserToPanel(@CurrentUser User user) {
    if (user == null || !user.getRole().equals(UserRoleType.USER)) {
      throw new UnAuthorizedException("");
    }

    return new ModelAndView("member/change_to_panel");
  }

}
