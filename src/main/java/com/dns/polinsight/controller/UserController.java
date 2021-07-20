package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.ChangePwdDto;
import com.dns.polinsight.domain.dto.SignupDTO;
import com.dns.polinsight.service.AdditionalService;
import com.dns.polinsight.service.ChangePasswordService;
import com.dns.polinsight.service.EmailService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
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

  private final ChangePasswordService changePasswordService;

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

  @DeleteMapping("/user")
  public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody User user) {
    Map<String, Object> map = new HashMap<>();
    try {
      userService.deleteUser(user);
      map.put("data", user.getEmail() + " has deleted");
      map.put("msg", user.getEmail() + " has deleted");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("error", null);
      map.put("msg", e.getMessage());
    }
    return ResponseEntity.ok(map);
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
    mv.setViewName("member/mypage");
    mv.addObject("user", userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build()));
    return mv;
  }

  @PostMapping("/update")
  public ResponseEntity<Map<String, Object>> updateUserInfo(@RequestBody User user, HttpSession session) {
    Map<String, Object> map = new HashMap<>();
    try {
      String password = passwordEncoder.encode(user.getPassword());
      user = userService.findUserByEmail(user);
      user.setPassword(password);
      userService.update(user);
      map.put("data", "user info has updated");
      session.invalidate();
      session.setAttribute("user", new SessionUser().of(user));
      log.info("User '{}' has requested change password", user.getEmail());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("error", "there is something wrong");
    }
    return ResponseEntity.ok(map);
  }

  @PostMapping("/findpwd")
  public ModelAndView findPwd(@Value("${custom.callback.base}") String callbackBase,
                              @RequestParam(name = "email") String email,
                              @RequestParam(name = "name") String name) throws MessagingException,
                                                                               NoSuchAlgorithmException {
    /*
     *  메일 서비스 필요
     *  등록한 주소로 메일 리다이렉팅 할 메일 전달
     * 해시값을 저장한 페이지를 리턴한다
     * 유저 이름, 이메일, 해시값을 디비에 저장해둔다.
     * */
    //    템플릿 작성에 필요한 변수들을 담는 객체
    ModelAndView mv = new ModelAndView("index");
    Map<String, Object> variables = new HashMap<>();
    String hash = userService.makeHashForChangePassword(email, name);
    changePasswordService.saveChangePwdDto(ChangePwdDto.builder().hash(hash).email(email).name(name).build());
    //    비밀번호 찾기를 요청한 유저에게 패스워드 변경을 위한 이메일 전송
    variables.put("date", LocalDateTime.now());
    variables.put("hash", hash);
    variables.put("name", name);
    variables.put("email", email);
    variables.put("callback", callbackBase + "/chpwd");
    emailService.sendTemplateMail(email, "폴인사이트에서 요청하신 비밀번호 변경 안내 메일입니다.", variables);
    return mv;
  }

  @GetMapping("/changepwd")
  public ModelAndView changePwd(HttpServletRequest request, HttpSession session, @LoginUser SessionUser sessionUser) {
    ModelAndView mv = new ModelAndView("member/changepwd");
    log.info(sessionUser.toString());


    if (session.getAttribute("user") == null) {
      return new ModelAndView("redirect:/denied");
    }
    return mv;
  }

  @PostMapping("/changepwd")
  public ModelAndView changePwd(@LoginUser SessionUser sessionUser,
                                HttpSession session,
                                @RequestAttribute(name = "pwd") String newPassword) {
    /*
     * 비밀번호 변경 처리 후 리다이렉팅
     * 유저로부터 정보가 넘어오면, 디비에서 이메일, 이름, 해시값을 확인하고 맞다면 비밀번호 변경 후 리다이렉팅
     * */
    Map<String, Object> map = new HashMap<>();
    log.info("New Password: {}", String.format("%20s", newPassword));
    try {
      User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
      user.setPassword(passwordEncoder.encode(newPassword));
      userService.update(user); // DB 업데이트할 유저 객체 넣기
      //      map.put("code", 200);
      //      map.put("msg", "비밀번호 변경 완료");
    } catch (Exception e) {
      //      map.put("code", 201);
      //      map.put("msg", "there is something worng");
    }

    session.invalidate();
    ModelAndView mv = new ModelAndView("redirect:/index");
    return mv;
  }

  @GetMapping("/chpwd/{hash}/{name}/{email}")
  public ModelAndView changePassword(@PathVariable(name = "hash") String hash,
                                     @PathVariable(name = "email") String email,
                                     @PathVariable(name = "name") String name,
                                     ModelAndView mv,
                                     HttpSession session
  ) {
    /*등록한 메일 주소로 전달한 페이지에서, 비밀번호 변경 클릭 시 리다이렉팅 될 주소*/
    //    log.info("callback url executed ---\n{}{}{}",
    //        String.format("%1$20s", "hash: " + hash),
    //        String.format("%1$20s", "email: " + email),
    //        String.format("%1$20s", "name: " + name)
    //    );
    mv.clear();
    mv = new ModelAndView("redirect:/changepwd");
    try {
      if (!hash.equals(changePasswordService.findChangePwdDtoByEmail(email).getHash())) {
        // 받은 해시와 저장된 해시가 다르면 접근 거부
        throw new IllegalAccessException();
      }
      session.setAttribute("user", new SessionUser().of(userService.findUserByEmail(User.builder().email(email).build())));

    } catch (Exception e) {
      e.printStackTrace();
      session.invalidate();
      ModelAndView errMav = new ModelAndView("redirect:error/denied");
      errMav.addObject("msg", e.getCause());
      return errMav;
    }

    return mv;
  }

  // TODO: 2021-07-19 수정 필요
  @GetMapping("/req/point/{userid}/{point}")
  public ResponseEntity<Map<String, Object>> requestPointCalcFromUser(@PathVariable(name = "userid") String userid,
                                                                      @PathVariable(name = "point") Long pint) {
    /*
     * 포인트 발급 요청 로그 남기기 --> 관리자 페이지에서 보여줄 것
     * */
    Map<String, Object> map = new HashMap<>();
    return ResponseEntity.ok(map);
  }

}
