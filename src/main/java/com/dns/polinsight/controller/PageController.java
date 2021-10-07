package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.UnAuthorizedException;
import com.dns.polinsight.service.PostService;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.PermitAll;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PageController {

  private final PostService postService;

  @PermitAll
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

  @PermitAll
  @GetMapping("/login")
  public ModelAndView login(@CurrentUser User user) {
    if (user == null) {
      return new ModelAndView("member/login");
    } else {
      return new ModelAndView("redirect:/");
    }
  }

  @PermitAll
  @GetMapping("/join")
  public ModelAndView signUp(@CurrentUser User user) {
    if (user == null) {
      return new ModelAndView("member/join");
    } else {
      return new ModelAndView("redirect:/");
    }

  }

  @PermitAll
  @GetMapping("/signup")
  public ModelAndView contract(@CurrentUser User user) {
    if (user == null) {
      return new ModelAndView("member/contract");
    } else {
      return new ModelAndView("redirect:/");
    }

  }

  @GetMapping("/denied")
  public ModelAndView deniedHandler() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("error/denied");
    return mv;
  }

  @PermitAll
  @GetMapping("/success_basic")
  public ModelAndView successBasicMemberSignUp() {
    return new ModelAndView("member/success_basic");
  }

  @PermitAll
  @GetMapping("/success_panel")
  public ModelAndView successPanelMemberSignUp() {
    return new ModelAndView("member/success_panel");
  }

  //  @GetMapping("/events")
  //  public ModelAndView events() {
  //    return new ModelAndView("posts/events");
  //  }

  @PermitAll
  @GetMapping("/find")
  public ModelAndView find() {
    return new ModelAndView("member/find_info");
  }

  //  @GetMapping("/faq")
  //  public ModelAndView faq() {
  //    return new ModelAndView("posts/faq");
  //  }

  @GetMapping("/qna")
  public ModelAndView qna() {
    return new ModelAndView("posts/qna");
  }

  @PermitAll
  @GetMapping("/research/online")
  public String getResearchOnline(Model model) {
    model.addAttribute("checked", "online");
    return "research/onlineSurvey";
  }

  @PermitAll
  @GetMapping("/research/pols")
  public String getResearchPols(Model model) {
    model.addAttribute("checked", "pols");
    return "research/pols";
  }

  @PermitAll
  @GetMapping("/company/introduce")
  public String getCompanyIntroduce(Model model) {
    model.addAttribute("checked", "intro");
    return "company/introduce";
  }

  @PermitAll
  @GetMapping("/company/map")
  public String getCompanyMap(Model model) {
    model.addAttribute("checked", "map");
    return "company/map";
  }

  @PermitAll
  @GetMapping("/business/category")
  public String getBusinessCategory(Model model) {
    model.addAttribute("checked", "category");
    return "business/category";
  }

  @PermitAll
  @GetMapping("/business/result")
  public String getBusinessResult(Model model) {
    model.addAttribute("checked", "result");
    return "business/result";
  }

  @PermitAll
  @GetMapping("/accumulate_error")
  public ModelAndView pointAccumulateError() {
    return new ModelAndView("redirect:/point_accumulate_error");
  }

  @PreAuthorize("hasAnyAuthority('USER', 'PANEL')")
  @GetMapping("/point_accumulate_error")
  public ModelAndView pointAccumulateErrorPage() {
    return new ModelAndView("error/point_accumulate_error");
  }

  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/upgradepanel")
  public ModelAndView changeNormalUserToPanel(@CurrentUser User user) throws Exception {
    if (user == null) {
      throw new UnAuthorizedException("로그인 한 유저만 사용가능합니다.");
    }
    if (!user.getRole().equals(UserRoleType.USER)) {
      log.error("일반 유저만 사용 가능");
      throw new Exception("일반 유저만 사용 가능합니다");
    }

    return new ModelAndView("member/change_to_panel");
  }

  /**
   * pols -> insight페이지 (상세 보기)
   */
  @PermitAll
  @GetMapping("/insight/{postId}")
  public ModelAndView goInsightPage(@PathVariable("postId") String postId) {
    ModelAndView mv = new ModelAndView("posts/insight");
    mv.addObject("postId", postId);
    return mv;
  }

}
