package com.dns.polinsight.controller;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.PointCalculate;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.exception.WrongAccessException;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.PointCalculateService;
import com.dns.polinsight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipateSurveyController {

  private final PointCalculateService pointCalculateService;

  private final UserService userService;

  private final ParticipateSurveyService participateSurveyService;

  /*
   * 포인트 적립 성공 시 : mypage,
   * 포인트 적립 실패 시 : 적립 에러 팝업 페이지
   * */
  @GetMapping("/callback")
  public ModelAndView callback(
      //  public ModelAndView callback(@LoginUser SessionUser sessionUser,
      @RequestParam("hash") String hash,
      @RequestParam("email") String email) {
    if (hash.isBlank() || hash.isEmpty() || hash.equals("null")) {
      throw new InvalidParameterException();
    }
    try {
      // 해시값 체크, 유저 체크
      ParticipateSurvey participateSurvey = participateSurveyService.findBySurveyUserPairHash(hash).orElseThrow(SurveyNotFoundException::new);
      User user = userService.findById(participateSurvey.getUserId()).orElseThrow(UserNotFoundException::new);
      if (hash.equals(participateSurvey.getHash()) && email.equals(user.getEmail()) && user.getEmail().equals("testuser@gmail.com")) {
        //      if (hash.equals(participateSurvey.getHash()) && email.equals(user.getEmail()) && user.getEmail().equals(sessionUser.getEmail())) {
        // 적립 처리
        // 참여 서베이(finished필드를 true로), , 포인트히스토리에 남기기
        this.processingPointSurveyHistory(user, participateSurvey);
        log.info("user {} - finished survey {}", email, participateSurvey.getSurveyId());
        return new ModelAndView("redirect:/mypage");
      }
      throw new Exception();
    } catch (Exception | WrongAccessException e) {
      // todo 에러페이지 설정
      e.printStackTrace();
      return new ModelAndView("redirect:에러페이지로 리다이렉트");
    }
  }

  @Transactional
  public void processingPointSurveyHistory(User user, ParticipateSurvey participateSurvey) throws Exception, WrongAccessException {
    if (participateSurvey.getFinished()) {
      throw new WrongAccessException();
    }
    participateSurvey.setFinished(true);
    try {
      participateSurveyService.saveAndUpdate(participateSurvey);
    } catch (Exception e) {
      throw new Exception("SurveyHistory write Exception");
    }
    try {
      user.addParticipateSurvey(participateSurvey.getSurveyId());
      user.setPoint(user.getPoint() + participateSurvey.getSurveyPoint());
      user = userService.saveOrUpdate(user);
      System.out.println(user.toString());
    } catch (Exception e) {
      throw new Exception("User Info update Exception");
    }

    try {
      pointCalculateService.saveOrUpdate(PointCalculate.builder()
                                                       .amount(participateSurvey.getSurveyPoint())
                                                       .total(user.getPoint())
                                                       .sign(true)
                                                       .uid(user.getId())
                                                       .build());
    } catch (Exception e) {
      throw new Exception("PointHistory write Exception");
    }
  }

}
