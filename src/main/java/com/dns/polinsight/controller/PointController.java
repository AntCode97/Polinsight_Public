package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Point;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.PointNotUpdateException;
import com.dns.polinsight.service.PointService;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

  private final String salt = "polinsightPointSalt";

  private final UserService userService;

  private final PointService pointService;

  @GetMapping("/callback")
  public void callback(@RequestParam(value = "email", defaultValue = "null") String email, @RequestParam(value = "hash") String hash) {
    if (email.equals("null")) {
      throw new ValidationException("user mail");
    }
    User user = userService.findUserByEmail(User.builder().email(email).build());
    Point point = pointService.getHashByEmail(user);
    if (point.getHash().equals(hash)) {
      // 포인트 적립
      userService.update(user);
    } else {
      // 포인트 적립 실패
      // 실패 로그 작성
      throw new PointNotUpdateException("user " + email + " failed to accumulate points ");
    }
  }

  /*
   * 이메일로 해시 발급
   * */
  @GetMapping("/hash/{email}")
  public ModelAndView getHash(@LoginUser SessionUser sessionUser) {
    ModelAndView mv = new ModelAndView();
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.reset();
      digest.update(salt.getBytes(StandardCharsets.UTF_8));
      digest.update(sessionUser.getEmail().getBytes(StandardCharsets.UTF_8));
      mv.addObject("hash", String.format("%0128x", new BigInteger(1, digest.digest())));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return mv;
  }

  @GetMapping
  public ModelAndView pointInsquery(@LoginUser SessionUser sessionUser, HttpSession session) {
    ModelAndView mv = new ModelAndView();
    session.setAttribute("user", userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build()));
    return mv;
  }

}
