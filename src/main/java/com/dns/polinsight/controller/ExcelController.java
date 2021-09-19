package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.InvalidValueException;
import com.dns.polinsight.exception.UnAuthorizedException;
import com.dns.polinsight.mapper.UserMapping;
import com.dns.polinsight.service.ParticipateSurveyService;
import com.dns.polinsight.service.PointHistoryService;
import com.dns.polinsight.service.PointRequestService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExcelController {

  private final ParticipateSurveyService participateSurveyService;

  private final PointRequestService pointRequestService;

  private final PointHistoryService pointHistoryService;

  private final UserService userService;

  @GetMapping("/points/excel/{userId}")
  public void getExcelFromAllPointRequests(HttpServletResponse response,
                                           @CurrentUser User user,
                                           @PathVariable(value = "userId", required = false) Long userId) throws Exception {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    try {
      ExcelUtil<PointRequest> excelUtil = new ExcelUtil<>();
      if (userId == 0) {
        excelUtil.createExcelToResponse(pointRequestService.findAll(), String.format("%s-%s", "point_request", LocalDate.now()), response);
      } else {
        excelUtil.createExcelToResponse(pointRequestService.getUserPointRequests(userId), String.format("%s-%s", "point_request", LocalDate.now()), response);
      }
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
      log.error("포인트 전환 내역 다운로드 오류");
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/points/excel/count")
  public ApiUtils.ApiResult<Long> countExistsPointRequests(@CurrentUser User user) {
    log.warn("카운트 쿼리 ");
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    return success(pointRequestService.countExistsPointRequests());
  }

  @GetMapping("/points/excel/{userId}/count")
  public ApiUtils.ApiResult<Long> countExistsPointRequestsBYid(@CurrentUser User user,
                                                               @PathVariable("userId") Long userId) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    return success(pointRequestService.countExistsPointRequestsByUserId(userId));
  }

  @GetMapping("/pointhistory/excel")
  public void getExcelFromAllHistories(HttpServletResponse response,
                                       @CurrentUser User user,
                                       @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId) {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    try {
      ExcelUtil<PointHistory> excelUtil = new ExcelUtil<>();
      if (userId == 0) {
        excelUtil.createExcelToResponse(pointHistoryService.findAll(), String.format("%s-%s", "point_history", LocalDate.now()), response);
      } else {
        excelUtil.createExcelToResponse(pointHistoryService.findAllPointHistoryByUserId(userId), String.format("%s-%s", "point_history", LocalDate.now()), response);
      }
    } catch (IllegalAccessException | IOException e) {
      log.error("포인트 내역 다운로드 오류 : {}", e.getCause());
      throw new InvalidValueException(e.getMessage());
    }
  }

  @GetMapping("/pointhistory/excel/count")
  public ApiUtils.ApiResult<Long> countExistsPointhistories(@CurrentUser User user) {
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
      ExcelUtil<ParticipateSurvey> excelUtil = new ExcelUtil<>();
      if (userId == -1) {
        excelUtil.createExcelToResponse(participateSurveyService.findAll(), String.format("%s-%s", "participate_survey", LocalDate.now()), response);
      } else {
        excelUtil.createExcelToResponse(participateSurveyService.findAllByUserId(userId), String.format("%s-%s", "participate_survey", LocalDate.now()), response);
      }
    } catch (IllegalAccessException | IOException e) {
      log.error("참여설문 다운로드 오류 : {}", e.getCause());
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
    ExcelUtil<UserDto> excelUtil = new ExcelUtil<>();
    //    유저 목록 전체를 엑셀로 다운로드
    excelUtil.createExcelToResponse(userService.findAllUserToUserDto(), String.format("%s-%s", "point_history", LocalDate.now()), response);
  }

}
