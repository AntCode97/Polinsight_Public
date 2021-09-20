package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.ParticipateSurvey;
import com.dns.polinsight.domain.PointHistory;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.InvalidValueException;
import com.dns.polinsight.exception.UnAuthorizedException;
import com.dns.polinsight.mapper.ExcelUserMapping;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

  @GetMapping(value = "/points/excel/{userId}")
  public void getExcelFromAllPointRequests(HttpServletResponse response,
                                           @CurrentUser User user,
                                           @PathVariable(value = "userId", required = false) Long userId) throws Exception {
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    // TODO: 2021/09/20 : 로그 찍어보고 제대로 나오는지 확인하기
    // pathVariable 문제인거 같다
    log.warn("포인트 요청 목록 엑셀 다운로드");
    try {
      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("email", "이메일");
      headerMap.put("requestPoint", "요청 포인트");
      headerMap.put("requestedAt", "요청일");
      headerMap.put("bank", "은행");
      headerMap.put("account", "계좌번호");
      headerMap.put("name", "이름");
      headerMap.put("progress", "상태");
      ExcelUtil<PointRequest> excelUtil = new ExcelUtil<>();
      if (userId == 0) {
        excelUtil.createExcelToResponse(pointRequestService.findAll(), String.format("%s-%s", "point_request", LocalDate.now()), response, headerMap);
      } else {
        excelUtil.createExcelToResponse(pointRequestService.getUserPointRequests(userId), String.format("%s-%s", "point_request", LocalDate.now()), response, headerMap);
      }
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
      log.error("포인트 전환 내역 다운로드 오류");
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping(value = "/points/excel/count")
  public ApiUtils.ApiResult<Long> countExistsPointRequests(@CurrentUser User user) throws Exception {
    log.warn("카운트 쿼리 ");
    if (user == null) {
      throw new UnAuthorizedException("로그인하지 않으면 사용할 수 없습니다.");
    }
    try {
      Long count = pointRequestService.countExistsPointRequests();
      log.warn("카운트 결과 {}", count);
      return success(count);
    } catch (Exception e) {
      e.printStackTrace();
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
      // TODO : 사용자를 가져와야 함
      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("content", "내용");
      headerMap.put("userId", "사용자"); // 사용자 가져오기
      headerMap.put("amount", "변동 포인트");
      headerMap.put("sign", "변동 타입");
      headerMap.put("total", "사용자 전체 포인트");
      headerMap.put("requestedAt", "요청일");
      ExcelUtil<PointHistory> excelUtil = new ExcelUtil<>();
      if (userId == 0) {
        excelUtil.createExcelToResponse(pointHistoryService.findAll(), String.format("%s-%s", "point_history", LocalDate.now()), response, headerMap);
      } else {
        excelUtil.createExcelToResponse(pointHistoryService.findAllPointHistoryByUserId(userId), String.format("%s-%s", "point_history", LocalDate.now()), response, headerMap);
      }
    } catch (IllegalAccessException | IOException e) {
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
      Map<String, String> headerMap = new HashMap<>();
      headerMap.put("user", "참여자");
      headerMap.put("survey", "설문");
      headerMap.put("participatedAt", "참여일");
      headerMap.put("surveyPoint", "포인트");
      headerMap.put("finished", "종료 여부");


      ExcelUtil<ParticipateSurvey> excelUtil = new ExcelUtil<>();
      if (userId == -1) {
        excelUtil.createExcelToResponse(participateSurveyService.findAll(), String.format("%s-%s", "participate_survey", LocalDate.now()), response, headerMap);
      } else {
        excelUtil.createExcelToResponse(participateSurveyService.findAllByUserId(userId), String.format("%s-%s", "participate_survey", LocalDate.now()), response, headerMap);
      }
    } catch (IllegalAccessException | IOException e) {
      log.error("참여한 설문 다운로드 오류 : {}", e.getCause());
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
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("point", "포인트");
    headerMap.put("email", "이메일");
    headerMap.put("phone", "연락처");
    headerMap.put("recommend", "추천인 연락처");
    headerMap.put("address", "주소");
    headerMap.put("name", "이름");
    headerMap.put("registeredAt", "가입일");
    headerMap.put("isEmailReceive", "이메일 수신 여부");
    headerMap.put("isSmsReceive", "SMS 수신 여부");
    headerMap.put("birth", "생일");
    headerMap.put("birthType", "생일 구분");
    headerMap.put("job", "직업");
    headerMap.put("industry", "직종");

    ExcelUtil<ExcelUserMapping> excelUtil = new ExcelUtil<>();
    //    유저 목록 전체를 엑셀로 다운로드
    excelUtil.createExcelToResponse(userService.findAll().parallelStream().map(ExcelUserMapping::of).collect(Collectors.toList()),
        String.format("%s-%s", "member_list", LocalDate.now()), response, headerMap);
  }

}
