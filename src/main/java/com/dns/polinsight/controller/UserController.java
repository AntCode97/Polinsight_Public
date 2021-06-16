package com.dns.polinsight.controller;

import com.dns.polinsight.domain.SocialType;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

  @PostMapping("/deleteaccount")
  public ModelAndView deleteUser(User user) {
    ModelAndView mv = new ModelAndView();
    service.deleteUser(user);
    mv.setViewName("index");
    return mv;
  }

  @GetMapping("/user/{email}")
  @CrossOrigin("*") // 비동기 이메일 검증을 위한 cors 처리
  public ResponseEntity<Map<String, Object>> findUserByEmail(@PathVariable("email") String email) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("user", service.findUserByEmail(User.builder().email(email).build()));
    } catch (RuntimeException e) {
      e.getMessage();
      // NOTE 2021/06/12 : 아무것도 없는 응답을 보냄
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(map);
  }

}
