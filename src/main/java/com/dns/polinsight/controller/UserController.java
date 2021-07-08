package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.SignupDTO;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import com.dns.polinsight.object.ResponseObject;
import com.dns.polinsight.service.AdditionalService;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final AdditionalService additionalService;

  private final HttpSession session;

  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void saveUserDate() {
    userService.save(User.builder()
                         .email("test@gmail.com")
                         .name("TEST_NAME")
                         .password(passwordEncoder.encode("!@#$%QWERT"))
                         .role(UserRole.ADMIN)
                         .phone("01012345678")
                         .build());
  }

  @PostMapping("/signup")
  public ResponseEntity<Map<String, Object>> userSignUp(@RequestBody SignupDTO signupDTO) {
    Map<String, Object> map = new HashMap<>();
    try {
      User user = userService.save(User.builder()
                                       .email(signupDTO.getEmail())
                                       .name(signupDTO.getName())
                                       .phone(signupDTO.getPhone())
                                       .password(passwordEncoder.encode(signupDTO.getPassword()))
                                       .recommend(signupDTO.getRecommend())
                                       .role(UserRole.USER)
                                       .build());
      session.setAttribute("user", new SessionUser(user));
      if (signupDTO.isIspanel()) {
        map.put("code", 200);
        map.put("msg", "need more info for panel signup");
      } else {
        map.put("code", 200);
        map.put("msg", "normal user signup success");
      }
    } catch (Exception e) {
      log.error("basic singup error: {}", e.getMessage());
      map.put("code", 6000);
      map.put("msg", "there is something wrong");
    }
    return ResponseEntity.ok(map);
  }

  @PostMapping("/moreinfo")
  @Transactional
  public ResponseEntity<Map<String, Object>> panelSignup(@RequestBody Additional additional, @LoginUser SessionUser sessionUser, HttpSession session) {

    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
    additional.update(user);
    user.update(additional);
    userService.save(user);
    additionalService.save(additional);
    Map<String, Object> map = new HashMap<>();
    map.put("msg", "success");
    map.put("code", 200);
    session.setAttribute("user", sessionUser);

    return ResponseEntity.ok(map);

  }

  @PostMapping("/deleteaccount")
  public ModelAndView deleteUser(@RequestBody User user) {
    ModelAndView mv = new ModelAndView();
    userService.deleteUser(user);
    mv.setViewName("index");
    return mv;
  }

  @GetMapping("/user/{email}")
  @CrossOrigin("*") // 비동기 이메일 검증을 위한 cors 처리
  public ResponseEntity<Map<String, Object>> findUserByEmail(@Email @PathVariable("email") String email) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("user", userService.findUserByEmail(User.builder().email(email).build()));
    } catch (RuntimeException e) {
      e.getMessage();
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(map);
  }


  @GetMapping("/mypage")
  public ModelAndView myPage(@LoginUser SessionUser user) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("mypage");
    mv.addObject("user", userService.findUserByEmail(User.builder()
                                                         .email(user.getEmail())
                                                         .name(user.getName())
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
