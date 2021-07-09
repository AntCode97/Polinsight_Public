package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.SignupDTO;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.object.ResponseObject;
import com.dns.polinsight.service.AdditionalService;
import com.dns.polinsight.service.EmailService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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

  private final EmailService emailService;

  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void saveUserDate() {
    userService.save(User.builder()
                         .email("test@gmail.com")
                         .name("TEST_NAME")
                         .password(passwordEncoder.encode("!@#$%QWERT"))
                         .role(UserRoleType.ADMIN)
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
                                       .role(UserRoleType.USER)
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
  public ResponseEntity<?> findPwd(HttpServletRequest request) throws MessagingException {
    /*
     *  메일 서비스 필요
     *  등록한 주소로 메일 리다이렉팅 할 메일 전달
     * 해시값을 저장한 페이지를 리턴한다
     * 유저 이름, 이메일, 해시값을 디비에 저장해둔다.
     * */

    String emal = request.getParameter("email");
    String name = request.getParameter("name");
    emailService.sendMail("to", "subject", "body");
    ResponseObject obj = ResponseObject.builder()
                                       .statuscode(HttpStatus.OK.value())
                                       .msg("password changed")
                                       .build();
    return ResponseEntity.ok(obj);
  }

  @PostMapping("/chngepwd")
  public ResponseEntity<?> changePwd() {
    /*
     * 비밀번호 변경 처리 후 리다이렉팅
     * 유저로부터 정보가 넘어오면, 디비에서 이메일, 이름, 해시값을 확인하고 맞다면 비밀번호 변경 후 리다이렉팅
     * */
    String hash = "";
    String userName = "";
    String userEmail = "";
    // passwordChangeService.get();
    // PassworDTO 생성??
    Map<String, Object> map = new HashMap<>();

    try {
      userService.update(User.builder().build()); // DB 업데이트할 유저 객체 넣기
      map.put("code", 200);
      map.put("msg", "유저의 비밀번호가 변경되었습니다.");
    } catch (Exception e) {
      map.put("code", 201);
      map.put("msg", "there is something worng");
    }

    return ResponseEntity.ok(map);
  }


}
