package com.dns.polinsight.controller;

import com.dns.polinsight.domain.SocialType;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  private final PasswordEncoder passwordEncoder;

  @GetMapping("/loginpage")
  public ModelAndView login() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("login");
    return mv;
  }

  @PostMapping("/signup")
  public void userSignUp(User user, HttpServletResponse response) {
    try {
      service.save(User.builder()
                       .email(user.getEmail())
                       .password(passwordEncoder.encode(user.getPassword()))
                       .name(user.getName())
                       .role(UserRole.USER)
                       .social(SocialType.OWN)
                       .build());

      response.sendRedirect("/");
    } catch (IOException e) {
      e.printStackTrace();
      // TODO: 2021-06-02 : Alert Error to user
    }
  }

  @PostMapping("/logout")
  public void userLogOut() {

  }

}
