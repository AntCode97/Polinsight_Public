package com.dns.polinsight.controller;

import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.exception.WrongAccessException;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.PointHistoryService;
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
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipateSurveyController {

  private final PointHistoryService pointHistoryService;

  private final UserService userService;

  private final ParticipateSurveyService participateSurveyService;

  /*
   * 포인트 적립 성공 시 : mypage,
   * 포인트 적립 실패 시 : 적립 에러 팝업 페이지
   * */
  @GetMapping("/callback")
  public ModelAndView callback(
      @RequestParam("hash") String hash,
      @RequestParam("email") String email) {
    if (hash.isBlank() || hash.isEmpty() || hash.equals("null")) {
      throw new InvalidParameterException();
    }
    try {
      ParticipateSurvey participateSurvey = participateSurveyService.findBySurveyUserPairHash(hash).orElseThrow(SurveyNotFoundException::new);
      User user = userService.findById(participateSurvey.getUser().getId()).orElseThrow(UserNotFoundException::new);
      if (hash.equals(participateSurvey.getHash()) && email.toString().equals(user.getEmail().toString()) && user.getEmail().equals(participateSurvey.getUser().getEmail())) {
        // 적립 처리
        this.processingPointSurveyHistory(user, participateSurvey);
        log.info("user {} - finished survey {}", email, participateSurvey.getSurvey().getSurveyId());
        return new ModelAndView("redirect:/mypage");
      }
      throw new Exception();
    } catch (Exception | WrongAccessException e) {
      log.error(e.getMessage());
      return new ModelAndView("redirect:error/point_accumulate_error");
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
      user.getParticipateSurvey().add(participateSurvey);
      user.setPoint(user.getPoint() + participateSurvey.getSurveyPoint());
      user = userService.saveOrUpdate(user);
      System.out.println(user.toString());
    } catch (Exception e) {
      throw new Exception("User Info update Exception");
    }

    try {
      pointHistoryService.saveOrUpdate(PointHistory.builder()
                                                   .amount(participateSurvey.getSurveyPoint())
                                                   .total(user.getPoint())
                                                   .sign(true)
                                                   .content("설문 참여 보상")
                                                   .requestedAt(LocalDateTime.now())
                                                   .userId(user.getId())
                                                   .build());
    } catch (Exception e) {
      throw new Exception("PointHistory write Exception");
    }
  }

}
