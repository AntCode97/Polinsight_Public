package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.InvalidValueException;
import com.dns.polinsight.exception.UnAuthorizedException;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.PointHistoryService;
import com.dns.polinsight.service.PointRequestService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExcelController {

  private final ExcelUtil excelUtil;

  private final ParticipateSurveyService participateSurveyService;

  private final PointRequestService pointRequestService;

  private final PointHistoryService pointHistoryService;

  private final UserService userService;

  @GetMapping(value = "/points/excel")
  public void getExcelFromAllPointRequests(HttpServletResponse response,
                                           @CurrentUser User user,
                                           @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId) throws Exception {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }

    try {

      if (userId == 0) {
        List<PointRequest> data = pointRequestService.findAll();
        excelUtil.createPointRequestExcel(data, response, String.format("%s-%s", "point_request", LocalDate.now()));
      } else {
        excelUtil.createPointRequestExcel(pointRequestService.getUserPointRequests(userId), response, String.format("%s-%s", "point_request", LocalDate.now()));
      }
    } catch (IllegalAccessException | IOException e) {
      log.error("{} :: {}", e.getMessage(), "포인트 전환 내역 다운로드 오류");
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping(value = "/points/excel/count")
  public ApiUtils.ApiResult<Long> countExistsPointRequests(@CurrentUser User user) throws Exception {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    try {
      Long count = pointRequestService.countExistsPointRequests();
      return success(count);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/points/excel/{userId}/count")
  public ApiUtils.ApiResult<Long> countExistsPointRequestsBYid(@CurrentUser User user,
                                                               @PathVariable("userId") Long userId) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    Long count = pointRequestService.countExistsPointRequestsByUserId(userId);
    log.warn("카운트 결과 : {}", count);
    return success(count);
  }

  @GetMapping("/pointhistory/excel")
  public void getExcelFromAllHistories(HttpServletResponse response,
                                       @CurrentUser User user,
                                       @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }

    try {
      if (userId == 0) {
        excelUtil.createExcelPointHistory(pointHistoryService.findAll(), response, String.format("%s-%s", "point_history", LocalDate.now()));
      } else if (userId > 0) {
        User tmpUser = userService.findById(userId).get();
        excelUtil.createExcelPointHistory(pointHistoryService.findAllByUser(tmpUser), response, String.format("%s-%s", "point_history", LocalDate.now()));
      } else {
        excelUtil.createExcelPointHistory(pointHistoryService.findAllByUser(user), response, String.format("%s-%s", "point_history", LocalDate.now()));
      }
    } catch (IOException e) {
      log.error("포인트 내역 다운로드 오류 : {}", e.getCause());
      throw new InvalidValueException(e.getMessage());
    }
  }

  @GetMapping("/pointhistory/excel/count")
  public ApiUtils.ApiResult<Long> countExistsPointHistoryList(@CurrentUser User user) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    return success(pointHistoryService.countPointHistories());
  }

  @GetMapping("/participates/excel")
  public void getExcelFromAllParticipates(HttpServletResponse response,
                                          @CurrentUser User user,
                                          @RequestParam(value = "userId", required = false, defaultValue = "-1") Long userId) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    try {
      if (userId == -1) {
        excelUtil.createParticipateSurveyExcel(participateSurveyService.findAll(), response, String.format("%s-%s", "participate_survey", LocalDate.now()));
      } else {
        excelUtil.createParticipateSurveyExcel(participateSurveyService.findAllByUserId(userId), response, String.format("%s-%s", "participate_survey", LocalDate.now()));
      }
    } catch (IOException e) {
      log.error("참여 설문 다운로드 오류 : {}", e.getCause());
      throw new InvalidValueException(e.getMessage());
    }
  }

  @GetMapping("/participates/excel/count")
  public ApiUtils.ApiResult<Long> countExistParticipateSurvey(@CurrentUser User user) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    return success(participateSurveyService.countExistParticipateSurvey());
  }

  @GetMapping("/user/excel/count")
  public ApiUtils.ApiResult<Long> countExistUser(@CurrentUser User user) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    return success(userService.countAllUser());
  }

  @GetMapping("/user/excel")
  public void getUserDataByExcel(@CurrentUser User user,
                                 HttpServletResponse response) throws IOException, IllegalAccessException {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }


    //    유저 목록 전체를 엑셀로 다운로드
    excelUtil.createUserExcel(userService.findAll(), response, String.format("%s-%s", "member_list", LocalDate.now()));
  }

}
