package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.SurveyQuery;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.PointNotUpdateException;
import com.dns.polinsight.service.SurveyQueryService;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class SurveyQueryController {


  private final UserService userService;

  private final SurveyQueryService surveyQueryService;

  // TODO: 2021-07-06 0006 : 로직 변경 필요 
  @GetMapping("/callback")
  public ResponseEntity<?> callback(@ModelAttribute SurveyQuery surveyQuery) {
    Map<String, Object> map = new HashMap<>();

    log.info("emai: {}, hash: {}", surveyQuery.getEmail(), surveyQuery.getHash());

    System.out.printf("emai: {}, hash: {}%n", surveyQuery.getEmail(), surveyQuery.getHash());

    if (surveyQuery.getHash().equals("null")) {
      throw new ValidationException("user mail");
    }
    User user = userService.findUserByEmail(User.builder().email(surveyQuery.getEmail()).build());
    surveyQuery = surveyQueryService.getHashByEmail(user);
    if (surveyQuery.getHash().equals(surveyQuery.getHash())) {
      // 포인트 적립
      map.put("msg", "success");
      map.put("data", userService.save(user));
    } else {
      // 포인트 적립 실패
      // 실패 로그 작성
      throw new PointNotUpdateException("user " + surveyQuery.getEmail() + " failed to accumulate points ");
    }
    return ResponseEntity.ok(map);
  }

  /*
   * 이메일로 해시 발급
   * */
  @GetMapping("/hash/{email}")
  public ModelAndView getHash(@LoginUser SessionUser sessionUser) {
    return null;
  }

  @GetMapping
  public ResponseEntity<?> pointInsquery(@LoginUser SessionUser sessionUser, HttpSession session) {
    Map<String, Object> map = new HashMap<>();
    User user = userService.findUserByEmail(User.builder().email(sessionUser.getEmail()).build());
    sessionUser = sessionUser.of(user);
    session.setAttribute("user", sessionUser);
    map.put("msg", "success");
    map.put("result", sessionUser.getPoint());
    return ResponseEntity.ok(map);
  }

}
