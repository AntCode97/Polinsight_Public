package com.dns.polinsight.controller;

import com.dns.polinsight.domain.*;
import com.dns.polinsight.domain.dto.ChangePwdDto;
import com.dns.polinsight.domain.dto.ParticipateSurveyDto;
import com.dns.polinsight.domain.dto.SignupDTO;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.DataUpdateException;
import com.dns.polinsight.exception.InvalidValueException;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.exception.WrongAccessException;
import com.dns.polinsight.service.*;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import com.dns.polinsight.types.UserRoleType;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final ParticipateSurveyService participateSurveyService;

  private final PointRequestService pointRequestService;

  private final EmailService emailService;

  private final PasswordEncoder passwordEncoder;

  private final ChangePasswordService changePasswordService;

  private final LoginService loginService;

  @Transactional
  @PostMapping("/signup")
  public ApiUtils.ApiResult<Boolean> userSignUp(@RequestBody SignupDTO signupDTO,
                                                ModelAndView mv) throws Exception {
    try {
      log.warn("Sing up Info - " + signupDTO.toString());
      signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
      User user = userService.saveOrUpdate(new User(signupDTO));
      //      UserDetails userDetails = userService.loadUserByUsername(user.getEmail().toString());
      //      log.warn("Saved Info - " + user);
      //      loginService.login(userDetails.getUsername(), inputPassword);
      //      request.login(user.getEmail().toString(), inputPassword);
      //      log.warn(SecurityContextHolder.getContext().getAuthentication().getName());

      //      if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
      //        log.warn("유저 로그인 성공!!!");
      //        session.setAttribute("user", new UserDto(user));
      //      } else {
      //        log.warn("유저 로그인 실패");
      //      }
      if (signupDTO.isIspanel()) {
        mv.setViewName("redirect:/panel");
      } else {
        mv.addObject("name", user.getName());
        mv.addObject("email", user.getEmail());
        mv.setViewName("redirect:/success_basic");
      }
      return success(Boolean.TRUE);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  @PostMapping("/panel_signup")
  @Transactional
  public ApiUtils.ApiResult<Boolean> panelSignup(@RequestBody Panel panel, @AuthenticationPrincipal User user) throws WrongAccessException, DataUpdateException {
    if (user.getRole() != UserRoleType.USER) {
      throw new WrongAccessException("패널 가입은 일반 유저만 사용이 가능합니다.");
    }
    try {
      user.setPanel(panel);
      userService.saveOrUpdate(user);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      e.printStackTrace();
      throw new DataUpdateException("패널 정보를 업데이트하지 못하였습니다.");
    }
  }

  @Transactional
  @PostMapping("/user/delete")
  public void deleteUser(@AuthenticationPrincipal User user,
                         ModelAndView mv,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
    try {
      Long userId = user.getId();
      String email = user.getEmail().toString();
      userService.deleteUserById(user.getId());
      new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
      SecurityContextHolder.clearContext();
      log.info("ID : {} - {} has deleted", userId, email);
      //      return success(Boolean.TRUE);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  @Transactional
  @PutMapping("/user")
  public ApiUtils.ApiResult<UserDto> updateUser(@RequestBody UserDto userDto,
                                                @RequestParam(value = "type") String type,
                                                @AuthenticationPrincipal User user) throws Exception {
    if (type.isBlank())
      throw new InvalidValueException();

    try {
      log.warn(userDto.toString());
      switch (type) {
        case "basic":
          user.setName(userDto.getName());
          user.setPhone(Phone.of(userDto.getPhone()));
          break;
        case "password":
          if (!passwordEncoder.matches(userDto.getNewPassword(), user.getPassword()))
            user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
          else
            throw new InvalidValueException("이전 패스워드와 같은 패스워드는 사용할 수 없습니다");
          break;
        case "panel":
          user.getPanel().setFavorite(userDto.getFavorite());
          break;
      }

      log.warn("after processing : " + user.toString());
      return success(new UserDto(userService.saveOrUpdate(user)));
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  @Transactional
  @PutMapping("/admin/user")
  public ApiUtils.ApiResult<Boolean> adminUpdateUser(@RequestBody UserDto userDto) throws Exception {
    try {
      User tuser = userService.findById(userDto.getId()).get();
      User user = new User(userDto);
      user.setPassword(tuser.getPassword());
      userService.saveOrUpdate(user);
      return success(true);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/mypage")
  public ModelAndView myPage(@AuthenticationPrincipal User user, Authentication authentication) throws WrongAccessException, Exception {
    log.warn("mypage -- " + authentication.getPrincipal().toString());
    if (user == null)
      throw new WrongAccessException("잘못된 접근입니다.");

    try {
      ModelAndView mv = new ModelAndView();
      UserDto dto = new UserDto(userService.findUserByEmail(user.getEmail()));
      mv.setViewName("member/mypage");
      mv.addObject("user", dto);
      return mv;
    } catch (UsernameNotFoundException e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  @PostMapping("/update")
  public ResponseEntity<Map<String, Object>> updateUserInfo(@RequestBody User user, HttpSession session) {
    Map<String, Object> map = new HashMap<>();
    try {
      String password = passwordEncoder.encode(user.getPassword());
      user = userService.findUserByEmail(user.getEmail());
      user.setPassword(password);
      userService.saveOrUpdate(user);
      map.put("data", "user info has updated");
      session.invalidate();
      session.setAttribute("user", new UserDto(user));
      log.info("User '{}' has requested change password", user.getEmail());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("error", "there is something wrong");
    }
    return ResponseEntity.ok(map);
  }


  @GetMapping("/changepwd")
  public ModelAndView changePwd(HttpServletRequest request, HttpSession session, @AuthenticationPrincipal User user) {
    ModelAndView mv = new ModelAndView("member/changepwd");
    if (session.getAttribute("user") == null) {
      return new ModelAndView("redirect:/denied");
    }
    return mv;
  }

  @PostMapping("/changepwd")
  public ModelAndView changePwd(@AuthenticationPrincipal User user,
                                HttpSession session,
                                @RequestParam(name = "pwd") String newPassword) throws Exception {
    /*
     * 비밀번호 변경 처리 후 리다이렉팅
     * 유저로부터 정보가 넘어오면, 디비에서 이메일, 이름, 해시값을 확인하고 맞다면 비밀번호 변경 후 리다이렉팅
     * */
    session.invalidate();
    //    User user = userService.findUserByEmail(sessionUser.getEmail());
    user.setPassword(passwordEncoder.encode(newPassword));
    userService.saveOrUpdate(user); // DB 업데이트할 유저 객체 넣기
    return new ModelAndView("redirect:/index");
  }

  @GetMapping("/change/{hash}/{name}/{email}")
  public ModelAndView changePassword(@PathVariable(name = "hash") String hash,
                                     @PathVariable(name = "email") Email email,
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
      session.setAttribute("user", new UserDto(userService.findUserByEmail(email)));
    } catch (Exception e) {
      e.printStackTrace();
      session.invalidate();
      ModelAndView errMav = new ModelAndView("redirect:error/denied");
      errMav.addObject("msg", e.getCause());
      return errMav;
    }

    return mv;
  }

  @PostMapping("/api/point/{point}")
  public ApiUtils.ApiResult<List<PointRequest>> requestPointCalcFromUser(@AuthenticationPrincipal User user,
                                                                         @PathVariable(name = "point") Long point) throws Exception {
    /*
     * 포인트 발급 요청 로그 남기기 --> 관리자 페이지 및 마이페이지에서 보여줄 것
     * */
    try {
      //      User user = userService.findUserByEmail(sessionUser.getEmail());
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

  @GetMapping("/api/user/participate/{userid}")
  public ApiUtils.ApiResult<List<ParticipateSurveyDto>> getParticipateSurveyList(@PathVariable("userid") long userId, @PageableDefault Pageable pageable) throws Exception {
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("participatedAt"));
    try {
      return success(participateSurveyService.findAllByUserId(userId, pageable).parallelStream()
                                             .map(ParticipateSurveyDto::new)
                                             .sorted()
                                             .collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PostMapping("/api/survey/participate")
  public ApiUtils.ApiResult<Boolean> participateSurvey(@AuthenticationPrincipal User user, Survey survey) throws Exception {
    try {
      //      User user = userService.findUserByEmail(sessionUser.getEmail());
      user.getParticipateSurvey().add(ParticipateSurvey.builder().survey(survey).build());
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

  // @Deprecated
  @PostMapping("/findpwd")
  public ModelAndView findPwd(@Value("${custom.callback.base}") String callbackBase,
                              @RequestParam(name = "email") Email email,
                              @RequestParam(name = "name") String name,
                              @Value("${custom.hash.passwordsalt}") String salt) throws MessagingException,
                                                                                        NoSuchAlgorithmException {
    /*
     *  메일 서비스 필요
     *  등록한 주소로 메일 리다이렉팅 할 메일 전달
     * 해시값을 저장한 페이지를 리턴한다
     * 유저 이름, 이메일, 해시값을 디비에 저장해둔다.
     * */
    if (userService.isExistEmail(email)) {
      Map<String, Object> variables = new HashMap<>();
      String hash = new HashUtil().makeHash(Arrays.asList(email.toString(), name), salt);
      changePasswordService.saveChangePwdDto(ChangePwdDto.builder().hash(hash).email(email).name(name).build());
      //    비밀번호 찾기를 요청한 유저에게 패스워드 변경을 위한 이메일 전송
      variables.put("date", LocalDateTime.now());
      variables.put("hash", hash);
      variables.put("name", name);
      variables.put("email", email);
      variables.put("callback", callbackBase + "/chpwd");
      emailService.sendTemplateMail(email.toString(), "폴인사이트에서 요청하신 비밀번호 변경 안내 메일입니다.", variables);
    }
    return new ModelAndView("redirect:/index");
  }

  @PostMapping("/find")
  @Transactional
  public ApiUtils.ApiResult<Object> findEmailOrPassword(@RequestBody UserDto userDto,
                                                        @RequestParam("type") String type,
                                                        @Value("${custom.hash.passwordsalt}") String salt,
                                                        @Value("${custom.callback.base}") String callbackBase) throws Exception {
    try {
      if (type.equals("email")) {
        Assert.notNull(userDto.getName());
        Assert.notNull(userDto.getPhone());
        User user = userService.findUserEmailByNameAndPhone(userDto.getName(), Phone.of(userDto.getPhone())).orElseThrow(UserNotFoundException::new);
        return success(user.getEmail().toString());
      } else {
        Assert.notNull(userDto.getEmail());
        Assert.notNull(userDto.getName());
        User user = userService.findUserByEmail(Email.of(userDto.getEmail()));
        log.debug(user.getName());
        log.debug(userDto.getName());
        if (!user.getName().equals(userDto.getName()))
          throw new InvalidValueException("Invalid Value Exception");
        String hash = new HashUtil().makeHash(Arrays.asList(userDto.getEmail(), userDto.getName()), salt);
        Map<String, Object> variables = new HashMap<>();
        variables.put("date", LocalDateTime.now());
        variables.put("hash", hash);
        variables.put("name", userDto.getName());
        variables.put("email", userDto.getEmail());
        variables.put("callback", callbackBase + "/change");
        changePasswordService.saveChangePwdDto(ChangePwdDto.builder()
                                                           .hash(hash)
                                                           .email(Email.of(userDto.getEmail()))
                                                           .name(userDto.getName())
                                                           .build());
        emailService.sendTemplateMail(userDto.getEmail(), "폴인사이트에서 요청하신 비밀번호 변경 안내 메일입니다.", variables);
        return success(Boolean.TRUE);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

}
