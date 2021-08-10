package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.ChangePwdDto;
import com.dns.polinsight.domain.dto.SignupDTO;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.service.*;
import com.dns.polinsight.types.UserRoleType;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.HashUtil;
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
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final ParticipateSurveyService participateSurveyService;

  private final PointRequestService pointRequestService;

  private final HttpSession session;

  private final EmailService emailService;

  private final SurveyService surveyService;

  private final PasswordEncoder passwordEncoder;

  private final ChangePasswordService changePasswordService;

  @PostMapping("/signup")
  public ApiUtils.ApiResult<Boolean> userSignUp(@RequestBody SignupDTO signupDTO) throws Exception {
    System.out.println(signupDTO);
    try {
      User user = userService.saveOrUpdate(signupDTO.toUser(passwordEncoder));
      if (signupDTO.isIspanel()) {
        session.setAttribute("basic_user", new SessionUser(user));
      } else {
        session.setAttribute("user", new SessionUser(user));
      }
      return success(Boolean.TRUE);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  @PostMapping("/moreinfo")
  @Transactional
  public ApiUtils.ApiResult<Boolean> panelSignup(@RequestBody Additional additional, HttpSession session) {
    session.invalidate();
    SessionUser sessionUser = (SessionUser) session.getAttribute("basic_user");
    sessionUser = SessionUser.builder()
                             .email(sessionUser.getEmail())
                             .role(UserRoleType.PANEL)
                             .name(sessionUser.getName())
                             .point(sessionUser.getPoint())
                             .build();
    session.invalidate();
    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
    user.getAdditional().update(additional);
    userService.saveOrUpdate(user);
    return success(Boolean.TRUE);
  }

  @DeleteMapping("/user")
  public ApiUtils.ApiResult<Boolean> deleteUser(@RequestBody UserDto userDto) throws Exception {
    Map<String, Object> map = new HashMap<>();
    try {
      userService.deleteUserById(userDto.getId());
      return success(true);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PutMapping("/user")
  public ApiUtils.ApiResult<UserDto> updateUser(@RequestBody UserDto userDto) throws Exception {
    System.out.println(userDto.toString());
    try {
      return success(new UserDto(userService.saveOrUpdate(new User(userDto))));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
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
      userService.saveOrUpdate(user);
      map.put("data", "user info has updated");
      session.invalidate();
      session.setAttribute("user", new SessionUser(user));
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
                              @RequestParam(name = "name") String name,
                              @Value("${custom.hash.passwordsalt}") String salt) throws MessagingException,
                                                                                        NoSuchAlgorithmException {
    /*
     *  메일 서비스 필요
     *  등록한 주소로 메일 리다이렉팅 할 메일 전달
     * 해시값을 저장한 페이지를 리턴한다
     * 유저 이름, 이메일, 해시값을 디비에 저장해둔다.
     * */
    if (userService.isExistUser(email)) {
      Map<String, Object> variables = new HashMap<>();
      String hash = new HashUtil().makeHash(Arrays.asList(email, name), salt);
      changePasswordService.saveChangePwdDto(ChangePwdDto.builder().hash(hash).email(email).name(name).build());
      //    비밀번호 찾기를 요청한 유저에게 패스워드 변경을 위한 이메일 전송
      variables.put("date", LocalDateTime.now());
      variables.put("hash", hash);
      variables.put("name", name);
      variables.put("email", email);
      variables.put("callback", callbackBase + "/chpwd");
      emailService.sendTemplateMail(email, "폴인사이트에서 요청하신 비밀번호 변경 안내 메일입니다.", variables);
    }
    return new ModelAndView("redirect:/index");
  }

  @GetMapping("/changepwd")
  public ModelAndView changePwd(HttpServletRequest request, HttpSession session, @LoginUser SessionUser sessionUser) {
    ModelAndView mv = new ModelAndView("member/changepwd");
    if (session.getAttribute("user") == null) {
      return new ModelAndView("redirect:/denied");
    }
    return mv;
  }

  @PostMapping("/changepwd")
  public ModelAndView changePwd(@LoginUser SessionUser sessionUser,
                                HttpSession session,
                                @RequestParam(name = "pwd") String newPassword) throws Exception {
    /*
     * 비밀번호 변경 처리 후 리다이렉팅
     * 유저로부터 정보가 넘어오면, 디비에서 이메일, 이름, 해시값을 확인하고 맞다면 비밀번호 변경 후 리다이렉팅
     * */
    session.invalidate();
    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
    user.setPassword(passwordEncoder.encode(newPassword));
    userService.saveOrUpdate(user); // DB 업데이트할 유저 객체 넣기
    return new ModelAndView("redirect:/index");
  }

  @GetMapping("/chpwd/{hash}/{name}/{email}")
  public ModelAndView changePassword(@PathVariable(name = "hash") String hash,
                                     @PathVariable(name = "email") String email,
                                     @PathVariable(name = "name") String name,
                                     ModelAndView mv,
                                     HttpSession session
  ) {
    mv.clear();
    mv = new ModelAndView("redirect:/changepwd");
    try {
      if (!hash.equals(changePasswordService.findChangePwdDtoByEmail(email).getHash())) {
        // 받은 해시와 저장된 해시가 다르면 접근 거부
        throw new IllegalAccessException();
      }
      session.setAttribute("user", new SessionUser(userService.findUserByEmail(User.builder().email(email).build())));
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
  @PostMapping("/api/point/{point}")
  public ApiUtils.ApiResult<List<PointRequest>> requestPointCalcFromUser(@LoginUser SessionUser sessionUser,
                                                                         @PathVariable(name = "point") Long point) throws Exception {
    /*
     * 포인트 발급 요청 로그 남기기 --> 관리자 페이지 및 마이페이지에서 보여줄 것
     * */
    try {
      User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
      pointRequestService.addUserPointRequest(user.getId(), point);
      return success(pointRequestService.getUserPointRequests(user.getId()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/api/point/{userid}")
  public ApiUtils.ApiResult<List<PointRequest>> getPointRequestList(@PathVariable(name = "userid") long userid) throws Exception {
    try {
      return success(pointRequestService.getUserPointRequests(userid));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/api/user/participate")
  public ApiUtils.ApiResult<Set<Survey>> getParticipateSurveyList(@LoginUser SessionUser sessionUser) throws Exception {
    try {
      User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
      return success(surveyService.getUserParticipateSurvey(user));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PostMapping("/api/survey/participate")
  public ApiUtils.ApiResult<Boolean> participateSurvey(@LoginUser SessionUser sessionUser, Survey survey) throws Exception {
    try {
      User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
      user.addParticipateSurvey(survey.getId());
      userService.saveOrUpdate(user);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/api/user/total")
  public ApiUtils.ApiResult<Long> coutAllUser() {
    return success(userService.countAllUserExcludeAdmin());
  }


}
