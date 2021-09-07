package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.AlreadyParticipateSurveyException;
import com.dns.polinsight.exception.SurveyNotFoundException;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.exception.WrongAccessException;
import com.dns.polinsight.mapper.SurveyMapping;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.PointHistoryService;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipateSurveyController {

  private final PointHistoryService pointHistoryService;

  private final UserService userService;

  private final SurveyService surveyService;

  private final ParticipateSurveyService participateSurveyService;

  @GetMapping("/participates")
  public ApiUtils.ApiResult<List<ParticipateSurvey>> getParticipateSurveyByUserid(@CurrentUser User user) throws WrongAccessException {
    if (user == null) {
      throw new WrongAccessException();
    }
    return success(participateSurveyService.findAllByUserId(user.getId()));
  }

  @Transactional
  @GetMapping("/callback")
  public ModelAndView callback(
      @RequestParam("hash") String hash,
      @RequestParam("email") String name) {
    if (hash.isBlank() || hash.isEmpty() || hash.equals("null")) {
      throw new InvalidParameterException();
    }
    try {
      ParticipateSurvey participateSurvey = participateSurveyService.findBySurveyUserPairHash(hash).orElseThrow(SurveyNotFoundException::new);
      // 설문 종료 표시
      participateSurveyService.updateParticipateSurveyById(participateSurvey.getId());
      User user = userService.findById(participateSurvey.getUser().getId()).orElseThrow(UserNotFoundException::new);
      if (hash.equals(participateSurvey.getHash()) && name.equals(user.getEmail().toString()) && user.getEmail().equals(participateSurvey.getUser().getEmail())) {
        // 적립 처리
        this.processingPointSurveyHistory(user, participateSurvey);
        log.info("user {} - finished survey {}", name, participateSurvey.getSurvey().getSurveyId());
        return new ModelAndView("redirect:/");
      }
      throw new Exception();
    } catch (Exception | WrongAccessException e) {
      log.error(e.getMessage());
      e.printStackTrace();
      return new ModelAndView("redirect:/accumulate_error");
    }
  }

  @Transactional
  public void processingPointSurveyHistory(User user, ParticipateSurvey participateSurvey) throws Exception, WrongAccessException {
    if (participateSurvey.getFinished()) {
      throw new AlreadyParticipateSurveyException("이미 참여한 설문입니다.");
    }
    try {
      participateSurvey.setFinished(true);
      participateSurvey.addUser(user);
      participateSurveyService.saveAndUpdate(participateSurvey);
    } catch (Exception e) {
      throw new Exception("SurveyHistory write Exception");
    }
    try {
      Survey survey = participateSurvey.getSurvey();
      survey.updateCount();
      log.info("설문 참여 횟수 업데이트");
    } catch (Exception e) {
      throw new Exception("설문 카운트 갱신 오류");
    }
    try {
      user.addParticipateSurvey(participateSurvey);
      user.updatePoint(participateSurvey.getSurveyPoint());
      user = userService.saveOrUpdate(user);
      log.info("사용자 설문 참여 정보 업데이트");
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
      log.info("{} 설문 참여 {} 포인트 업데이트", user.getEmail().toString(), participateSurvey.getSurveyPoint());
    } catch (Exception e) {
      throw new Exception("PointHistory write Exception");
    }
  }


  /*
   * 로그인한 사용자가 서베이 클릭시
   * */
  @GetMapping("/participate/{surveyId}")
  public ApiUtils.ApiResult<String> surveyClickEventHandler(@CurrentUser User user,
                                                            @PathVariable("surveyId") Long id,
                                                            @Value("{custom.hash.pointsalt}") String salt) throws NoSuchAlgorithmException, AlreadyParticipateSurveyException {
    if (user == null) {
      throw new BadCredentialsException("UnAuthorized");
    }

    try {
      List<ParticipateSurvey> list = participateSurveyService.findAllByUserId(user.getId()).stream()
                                                             .filter(ParticipateSurvey::getFinished)
                                                             .filter(ps -> ps.getSurvey().getId().equals(id))
                                                             .collect(Collectors.toList());
      if (list.size() > 0) {
        throw new AlreadyParticipateSurveyException("이미 참여한 설문입니다.");
      }

      SurveyMapping survey = surveyService.findSurveyById(id);

      log.info("{} participate survey that is : {}", user.getEmail(), survey.getTitle());
      LocalDateTime now = LocalDateTime.now();
      List<String> someVariables = Arrays.asList(user.getEmail().toString(), survey.getSurveyId().toString(), now.toString());
      String hash = new HashUtil().makeHash(someVariables, salt);
      String sb = survey.getParticipateUrl() + "?hash=" + hash + "&email=" + user.getEmail();
      log.info("hash : {}, email : {}", hash, user.getEmail().toString());
      participateSurveyService.saveParticipateSurvey(ParticipateSurvey.builder()
                                                                      .survey(Survey.builder()
                                                                                    .surveyId(survey.getSurveyId())
                                                                                    .id(survey.getId())
                                                                                    .title(survey.getTitle())
                                                                                    .point(survey.getPoint())
                                                                                    .build())
                                                                      .hash(hash)
                                                                      .user(user)
                                                                      .participatedAt(now)
                                                                      .surveyPoint(survey.getPoint())
                                                                      .finished(false)
                                                                      .build());
      return success(sb);
    } catch (SurveyNotFoundException e) {
      e.printStackTrace();
      throw new SurveyNotFoundException(e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new NoSuchAlgorithmException(e.getMessage());
    }
  }

}
