package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import com.dns.polinsight.object.ResponseObject;
import com.dns.polinsight.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "user", description = "사용자 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  private final PasswordEncoder passwordEncoder;


  @PostMapping("/signup")
  public void userSignUp(User user, HttpServletResponse response) {
    try {
      service.save(User.builder()
                       .email(user.getEmail())
                       .password(passwordEncoder.encode(user.getPassword()))
                       .name(user.getName())
                       .role(UserRole.USER)
                       .build());

      response.sendRedirect("/");
    } catch (IOException e) {
      e.printStackTrace();
      // TODO: 2021-06-02 : Alert Error to user
    }
  }

  @PostMapping("/singup/panel")
  public ModelAndView panelSignup(User user, Session session, HttpServletResponse response) {
    ModelAndView mv = new ModelAndView();
    session.setAttribute("userSignUpInfo", user);
    mv.setViewName("panelsignup");
    return mv;

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


  @GetMapping("/mypage")
  public ModelAndView myPage(@LoginUser SessionUser user) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("mypage");
    mv.addObject("user", service.findUserByEmail(User.builder()
                                                     .email(user.getEmail())
                                                     .name(user.getName())
                                                     .picture(user.getPicture())
                                                     .build()));
    return mv;
  }

  @PostMapping("/findpwd")
  public ResponseEntity<?> findPwd() {
    // NOTE 2021-06-21 0021 : 패스워드를 바꾸는 방향으로 유도
    ResponseObject obj = ResponseObject.builder()
                                       .statuscode(HttpStatus.OK.value())
                                       .msg("password changed")
                                       .build();
    return ResponseEntity.ok(obj);
  }

  @PostMapping("/chngepwd")
  public ResponseEntity<?> changePwd() {
    return ResponseEntity.ok(null);
  }


}
