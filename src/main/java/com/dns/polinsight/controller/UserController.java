package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.SignupDTO;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.object.ResponseObject;
import com.dns.polinsight.repository.ChangePwdRepository;
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

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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

  private final ChangePwdRepository pwdRepository;

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
      session.setAttribute("basic_user", new SessionUser(user));
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
  public ResponseEntity<Map<String, Object>> panelSignup(@RequestBody Additional additional, HttpSession session) {
    //    session.invalidate();
    SessionUser sessionUser = (SessionUser) session.getAttribute("basic_user");
    sessionUser = SessionUser.builder()
                             .email(sessionUser.getEmail())
                             .role(UserRoleType.PANEL)
                             .name(sessionUser.getName())
                             .point(sessionUser.getPoint())
                             .build();
    session.invalidate();
    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
    //    user = User.builder().id(user.getId()).role(UserRoleType.PANEL).build();
    // 유저 객체 정보 업데이트
    additional.update(user);
    user = user.update(additional).update(sessionUser);
    // 업데이트된 정보 저장
    // 외래키를 갖는 Additional 엔티티의 객체가 먼저 저장되고 마스터 엔티티인 user가 저장되어야 한다.
    additionalService.save(additional);
    userService.save(user);

    Map<String, Object> map = new HashMap<>();
    map.put("msg", "success");
    map.put("code", 200);
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
  public ModelAndView myPage(@LoginUser SessionUser sessionUser) {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("mypage");
    mv.addObject("user", userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build()));
    return mv;
  }

  @PostMapping("/findpwd")
  public ResponseEntity<?> findPwd(HttpServletRequest request) throws MessagingException, NoSuchAlgorithmException {
    /*
     *  메일 서비스 필요
     *  등록한 주소로 메일 리다이렉팅 할 메일 전달
     * 해시값을 저장한 페이지를 리턴한다
     * 유저 이름, 이메일, 해시값을 디비에 저장해둔다.
     * */
    //    템플릿 작성에 필요한 변수들을 담는 객체
    Map<String, Object> variables = new HashMap<>();

    String email = request.getParameter("email");
    String name = request.getParameter("name");
    String hash = userService.getHash(email, name);

    //    비밀번호 찾기를 요청한 유저에게 패스워드 변경을 위한 이메일 전송
    variables.put("date", LocalDateTime.now());
    variables.put("hash", hash);
    variables.put("callback", "/changepassword");
    emailService.sendTemplateMail(email, "폴인사이트에서 요청하신 비밀번호 안내 메일입니다.", variables);
    // TODO: 2021-07-12 해시는 따로 저장
    ResponseObject obj = ResponseObject.builder()
                                       .statuscode(HttpStatus.OK.value())
                                       .msg("password changed")
                                       .build();
    return ResponseEntity.ok(obj);
  }

  @PostMapping("/changepwd")
  public ResponseEntity<?> changePwd(HttpServletRequest request, @LoginUser SessionUser sessionUser, HttpSession session) {
    /*
     * 비밀번호 변경 처리 후 리다이렉팅
     * 유저로부터 정보가 넘어오면, 디비에서 이메일, 이름, 해시값을 확인하고 맞다면 비밀번호 변경 후 리다이렉팅
     * */
    Map<String, Object> map = new HashMap<>();
    try {
      User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
      user.setPassword(passwordEncoder.encode(request.getParameter("newpassword")));
      userService.update(user); // DB 업데이트할 유저 객체 넣기
      map.put("code", 200);
      map.put("msg", "비밀번호 변경 완료");
    } catch (Exception e) {
      map.put("code", 201);
      map.put("msg", "there is something worng");
    }

    session.invalidate();
    return ResponseEntity.ok(map);
  }

  @GetMapping("/changepassword")
  public void changePassword(@RequestParam(name = "hash") String hash,
                             @RequestParam(name = "email") String email,
                             @RequestParam(name = "name") String name,
                             HttpSession session,
                             HttpServletResponse response
  ) throws NoSuchAlgorithmException, IOException {
    /*등록한 메일 주소로 전달한 페이지에서, 비밀번호 변경 클릭 시 리다이렉팅 될 주소*/
    // NOTE 2021-06-23 0023 : 해시가 우리 서버에서 발급한게 맞는지 확인한다
    log.info("callback url executed ---\n{}", String.format("%1$20s\n%1$20s\n%1$20s", "hash: " + hash, "email: " + email, "name: " + name));
    if (hash.equals(userService.getHash(email, name))) {
      session.setAttribute("user", new SessionUser().of(userService.findUserByEmail(User.builder().email(email).build())));
      response.sendRedirect("/changepwd");
    }
    response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
  }

}
