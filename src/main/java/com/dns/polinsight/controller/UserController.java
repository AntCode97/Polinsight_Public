package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.*;
import com.dns.polinsight.domain.dto.*;
import com.dns.polinsight.exception.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
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


  @PreAuthorize("isAnonymous()")
  @Transactional
  @PostMapping("/signup")
  public ApiUtils.ApiResult<Boolean> userSignUp(@RequestBody SignupDTO signupDTO,
                                                ModelAndView mv) {
    try {
      log.info("{} sing up" + signupDTO.getEmail());

      signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
      User user = userService.saveOrUpdate(new User(signupDTO));
      if (signupDTO.getIsPanel()) {
        mv.setViewName("redirect:/panel");
      } else {
        mv.addObject("name", user.getName());
        mv.addObject("email", user.getEmail());
        mv.setViewName("redirect:/success_basic");
      }
      return success(Boolean.TRUE);
    } catch (InvalidValueException e) {
      throw new InvalidValueException(e.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('USER')")
  @PostMapping("/panel_signup")
  @Transactional
  public ApiUtils.ApiResult<Boolean> panelSignup(@RequestBody Panel panel, @CurrentUser User user) throws WrongAccessException, DataUpdateException {
    if (user.getRole() != UserRoleType.USER) {
      throw new WrongAccessException("패널 가입은 일반 유저만 사용이 가능합니다.");
    }
    try {
      user.setPanel(panel);
      userService.saveOrUpdate(user);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new DataUpdateException("패널 정보를 업데이트하지 못하였습니다.");
    }
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional
  @PostMapping("/user/delete")
  public ApiUtils.ApiResult<Boolean> deleteUser(@CurrentUser User user,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
    try {
      String email = user.getEmail().toString();
      userService.deleteUserById(user.getId());
      SecurityContext context = SecurityContextHolder.getContext();
      new SecurityContextLogoutHandler().logout(request, response, context.getAuthentication());
      context.setAuthentication((Authentication) null);
      SecurityContextHolder.clearContext();
      log.info("ID : {} has deleted", email);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }


  @PreAuthorize("isAuthenticated()")
  @Transactional
  @PutMapping("/user")
  public ApiUtils.ApiResult<UserDto> updateUser(@RequestBody UserDto userDto,
                                                @RequestParam(value = "type") String type,
                                                @CurrentUser User user) throws Exception {
    if (type.isBlank())
      throw new InvalidValueException();

    try {
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

      return success(new UserDto(userService.saveOrUpdate(user)));
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional
  @PutMapping("/admin/user")
  public ApiUtils.ApiResult<Boolean> adminUpdateUser(@RequestBody UserDto dto) throws Exception {
    try {
      User tuser = userService.findById(dto.getId()).get();
      User user = new User(dto);
      user.setPassword(tuser.getPassword());
      userService.saveOrUpdate(user);
      log.info("{} information updated at {}", user.getEmail().toString(), LocalDateTime.now());
      return success(true);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAnyAuthority('USER','PANEL', 'BEST')")
  @GetMapping("/mypage")
  public ModelAndView myPage(@CurrentUser User user) throws WrongAccessException, Exception {
    if (user == null)
      throw new UnAuthorizedException("로그인한 유저만 사용 가능합니다.");

    try {
      ModelAndView mv = new ModelAndView();
      UserDto dto = new UserDto(user);
      mv.setViewName("member/mypage");
      mv.addObject("user", dto);
      return mv;
    } catch (UsernameNotFoundException e) {
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  @PostMapping("/update")
  @PreAuthorize("hasAnyAuthority('USER','PANEL', 'BEST')")
  public ResponseEntity<Map<String, Object>> updateUserInfo(@RequestBody User user, HttpSession session) throws Exception {
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
    } catch (InvalidValueException e) {
      log.error(e.getMessage());
      map.put("error", "there is something wrong");
      throw new Exception(e.getMessage());
    }
    return ResponseEntity.ok(map);
  }


  @PreAuthorize("isAnonymous()")
  @GetMapping("/changepwd")
  public ModelAndView changePwd(HttpSession session) {
    if (session.getAttribute("uid") == null) {
      log.error("UID Data Not Found");
      return new ModelAndView("redirect:/denied");
    }
    return new ModelAndView("member/change_password");
  }

  @PostMapping("/api/password/change")
  public ApiUtils.ApiResult<Boolean> changePwd(@RequestBody ChangeRequest changeRequest) throws Exception {
    try {
      User user = userService.findById(changeRequest.getId()).orElseThrow();
      user.setPassword(passwordEncoder.encode(changeRequest.getPassword()));
      userService.saveOrUpdate(user); // DB 업데이트할 유저 객체 넣기
      return success(Boolean.TRUE);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("isAnonymous()")
  @GetMapping("/change/{hash}/{name}/{email}")
  public ModelAndView changePassword(HttpSession session,
                                     @PathVariable(name = "hash") String hash,
                                     @PathVariable(name = "email") String email,
                                     @PathVariable(name = "name") String name) {
    try {
      log.info("changePassword() in UserController email : {}, name : {}, hash: {}", email, name, hash);
      if (!hash.equals(changePasswordService.findChangePwdDtoByEmail(Email.of(email)).getHash())) {
        // 받은 해시와 저장된 해시가 다르면 접근 거부
        log.error("해시값이 일치하지 않습니다");
        throw new IllegalAccessException();
      }
      User user = userService.findUserByEmail(Email.of(email));
      session.setAttribute("uid", user.getId());
      return new ModelAndView("redirect:/changepwd");
    } catch (IllegalAccessException e) {
      log.error(e.getMessage());
      ModelAndView errMav = new ModelAndView("redirect:/denied");
      errMav.addObject("msg", e.getCause());
      return errMav;
    }
  }

  @PreAuthorize("hasAnyAuthority('USER','PANEL','BEST')")
  @PostMapping("/api/point/{point}")
  public ApiUtils.ApiResult<List<PointRequest>> requestPointCalcFromUser(@CurrentUser User user,
                                                                         @PathVariable(name = "point") Long point) throws Exception {
    try {
      log.info("{} has left the request for point exchange", user.getEmail());
      pointRequestService.addUserPointRequest(user.getId(), point);
      return success(pointRequestService.getUserPointRequests(user.getId()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAnyAuthority('USER','PANEL','BEST')")
  @GetMapping("/api/point/{userid}")
  public ApiUtils.ApiResult<List<PointRequest>> getPointRequestList(@PathVariable(name = "userid") long userid) throws Exception {
    try {
      return success(pointRequestService.getUserPointRequests(userid));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAnyAuthority('USER','PANEL', 'BEST')")
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

  @PreAuthorize("hasAnyAuthority('USER','PANEL','BEST')")
  @PostMapping("/api/survey/participate")
  public ApiUtils.ApiResult<Boolean> participateSurvey(@CurrentUser User user, Survey survey) throws Exception {
    try {
      user.getParticipateSurvey().add(ParticipateSurvey.builder().survey(survey).build());
      userService.saveOrUpdate(user);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/api/user/total")
  public ApiUtils.ApiResult<Long> countAllUser() {
    return success(userService.countAllUserExcludeAdmin());
  }

  @PermitAll
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

  @PreAuthorize("isAnonymous()")
  @PostMapping("/find")
  @Transactional
  public ApiUtils.ApiResult<Object> findEmailOrPassword(@RequestBody UserDto userDto,
                                                        @RequestParam("type") String type,
                                                        @Value("${custom.hash.passwordsalt}") String salt,
                                                        @Value("${custom.callback.base}") String callbackBase) throws Exception {
    try {
      // 아이디 찾기
      if (type.equals("email")) {
        Assert.notNull(userDto.getName());
        Assert.notNull(userDto.getPhone());
        User user = userService.findUserEmailByNameAndPhone(userDto.getName(), Phone.of(userDto.getPhone())).orElseThrow(UserNotFoundException::new);
        return success(user.getEmail().toString());
      } else {
        // 비밀번호 찾기
        Assert.notNull(userDto.getEmail());
        Assert.notNull(userDto.getName());
        Assert.notNull(userDto.getPhone());
        User user = userService.findUserByEmail(Email.of(userDto.getEmail()));
        if (!user.getName().equals(userDto.getName()))
          throw new InvalidValueException("User naem is not matching the data in DB");
        if (!user.getPhone().toString().equals(userDto.getPhone())) {
          throw new InputMismatchException("Phone Number is not matching the data in DB");
        }
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
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  @PermitAll
  @Transactional
  @PostMapping("/user_join")
  public ApiUtils.ApiResult<Boolean> signup(
      @RequestBody SignupDTO signupDTO,
      HttpSession session) throws Exception {
    try {
      signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
      User tUser = new User(signupDTO);
      User user = userService.saveOrUpdate(tUser);
      session.setMaxInactiveInterval(60);
      session.setAttribute("name", user.getName());
      session.setAttribute("email", user.getEmail().toString());
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('USER')")
  @PatchMapping("/api/changetopanel")
  @Transactional
  public ApiUtils.ApiResult<Boolean> userChangeRoleToPanel(@RequestBody UserDto dto,
                                                           @CurrentUser User user) throws DataUpdateException {
    if (user == null) {
      throw new UnAuthorizedException("로그인한 유저만 사용 가능합니다.");
    }
    if (!user.getRole().equals(UserRoleType.USER)) {
      throw new UnAuthorizedException("일반 유저만 사용 가능합니다.");
    }
    log.info("{} has requested to change role", user.getEmail().toString());


    try {
      user.getPanel().update(Panel.of(dto));
      user.updateRole(UserRoleType.PANEL);
      userService.saveOrUpdate(user);
      // Spring security에 인증되어있는 객체를 업데이트
      Authentication before = SecurityContextHolder.getContext().getAuthentication();
      SecurityContextHolder.clearContext();
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new DataUpdateException("패널 변경중 에러 발생 :: " + e.getMessage());
    }
  }

}
