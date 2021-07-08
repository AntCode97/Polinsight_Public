package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.SignupDTO;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.object.ResponseObject;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  private final HttpSession session;

  private final PasswordEncoder passwordEncoder;

  //  @PostConstruct
  //  public void saveUserDate() {
  //    service.save(User.builder()
  //                     .email("albaneo0724@gmail.com")
  //                     .name("alban")
  //                     .password(passwordEncoder.encode("!@##$%QWERT"))
  //                     .role(UserRole.ADMIN)
  //                     .phone("010-1234-5678")
  //                     .build());
  //  }

  @PostMapping("/signup")
  public ResponseEntity<Map<String, Object>> userSignUp(@Valid SignupDTO signupDTO) {
    Map<String, Object> map = new HashMap<>();
    try {
      User user = service.save(signupDTO.toUser());
      session.setAttribute("user", user);
      if (signupDTO.getIspanel()) {
        map.put("code", 200);
        map.put("msg", "need more info for panel signup");
      } else {
        map.put("code", 200);
        map.put("msg", "normal user signup success");
      }
    } catch (Exception e) {
      map.put("code", 6000);
    }
    return ResponseEntity.ok(map);
  }

  @PostMapping("/moreinfo")
  public ResponseEntity<Map<String, Object>> panelSignup(@RequestBody User user, HttpSession session) {
    System.out.println(user.toString());

    Map<String, Object> map = new HashMap<>();
    map.put("msg", "success");
    map.put("code", 200);
    // TODO: 2021/07/08 write User.class -> SessionUser.class translation method
    session.setAttribute("user", user);

    return ResponseEntity.ok(map);

  }

  @PostMapping("/deleteaccount")
  public ModelAndView deleteUser(@Valid @RequestBody User user) {
    ModelAndView mv = new ModelAndView();
    service.deleteUser(user);
    mv.setViewName("index");
    return mv;
  }

  @GetMapping("/user/{email}")
  @CrossOrigin("*") // 비동기 이메일 검증을 위한 cors 처리
  public ResponseEntity<Map<String, Object>> findUserByEmail(@Valid @Email @PathVariable("email") String email) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("user", service.findUserByEmail(User.builder().email(email).build()));
    } catch (RuntimeException e) {
      e.getMessage();
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(map);
  }


  @GetMapping("/mypage")
  public ModelAndView myPage(@Valid @LoginUser SessionUser user) {
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
