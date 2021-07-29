package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.*;
import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.PointCalculateException;
import com.dns.polinsight.exception.PointHistoryException;
import com.dns.polinsight.service.*;
import com.dns.polinsight.types.PointRequestProgressType;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.ExcelUtil;
import com.dns.polinsight.utils.PageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

  private final UserService userService;

  private final SurveyService surveyService;

  private final AdminService adminService;

  private final ParticipateSurveyService participateSurveyService;

  private final PointRequestService pointRequestService;

  private final BoardService boardService;

  @PutMapping("{boardId}/count")
  public ApiUtils.ApiResult<Boolean> handleBoardCount(@PathVariable long boardId) throws Exception {
    try {
      Board board = boardService.findOne(boardId);
      board.getBoardStatus().setViewCount(board.getBoardStatus().getViewCount() + 1);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/surveys/sync")
  public ApiUtils.ApiResult<List<Survey>> surveySyncWithSM() throws Exception {
    try {
      // FIXME: 2021/07/26 : embedded 한 데이터가 넘어오지 않음
      return success(surveyService.getSurveyListAndSyncPerHour());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/user/find/{regex}")
  public ApiUtils.ApiResult<List<UserDto>> adminUserFind(@PathVariable(name = "regex") String regex) throws Exception {
    try {
      return success(adminService.adminSerchUserByRegex(regex).stream().map(UserDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @DeleteMapping("/user/{email}")
  public ApiUtils.ApiResult<Boolean> adminUserDeleteByEmail(@PathVariable(name = "email") String email) throws Exception {
    try {
      userService.deleteUserByEmail(email);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 회원 가입된 모든 유저 정보 반환
   * */
  @GetMapping("/users")
  public ApiUtils.ApiResult<List<UserDto>> adminFindAllUsers(@PageHandler Pageable pageable) throws Exception {
    try {
      System.out.println(pageable.toString());
      return success(userService.findAll(pageable).getContent().parallelStream().map(UserDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 모든 설문 반환
   * */
  @GetMapping("/surveys")
  public ApiUtils.ApiResult<List<Survey>> adminGetAllSurveys(@PageHandler Pageable pageable) throws Exception {
    try {
      return success(surveyService.findAll(pageable).getContent());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 정규식을 통한 다건 설문 검색
   * */
  @GetMapping("/survey/{regex}")
  public ApiUtils.ApiResult<List<Survey>> adminGetSurveyByRegex(@PathVariable(name = "regex") String regex, String type /*검색 타입*/) throws Exception {
    Map<String, Object> map = new HashMap<>();
    try {
      if (type.equals("title")) {
        return success(surveyService.findSurveysByTitleRegex(regex));
      } else {
        return success(surveyService.findSurveysByEndDate(LocalDateTime.parse(regex)));
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 설문 목록 삭제를 위한 api
   * */
  @DeleteMapping("/survey/{id}")
  public ApiUtils.ApiResult<Boolean> adminDeleteSurveyById(@PathVariable(name = "id") Long surveyId) throws Exception {
    try {
      surveyService.deleteSurveyById(surveyId);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 서베이 목록 수정을 위한 api
   * */
  @PutMapping("/survey")
  public ApiUtils.ApiResult<List<Survey>> adminUpdateSurveyById(Survey survey) throws Exception {
    try {
      surveyService.update(survey);
      return success(surveyService.findAll());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 사용자가 참여한 서베이 목록 가져옴
   * */
  @GetMapping("participate")
  public ApiUtils.ApiResult<List<ParticipateSurvey>> getUserParticipateSurvey(@LoginUser SessionUser sessionUser, @RequestParam("type") String type) throws Exception {
    try {
      return success(participateSurveyService.findByUserId(sessionUser.getId()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("{userId}/pointrequest")
  public ApiUtils.ApiResult<List<PointRequestDto>> getAllRequestOfUser(@LoginUser SessionUser sessionUser) throws Exception {
    try {
      return success(pointRequestService.getUserPointRequests(sessionUser.getId()).stream().map(PointRequestDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("{userId}/pointrequestlist/excel")
  public void getExcelFromAllRequests(HttpServletResponse response,
                                      @PathVariable("userId") long userId,
                                      @RequestBody(required = false) PointRequest pointRequest) {
    try {
      ExcelUtil<PointRequest> excelUtil = new ExcelUtil<>();
      excelUtil.createExcelToResponse(pointRequestService.getUserPointRequests(userId), String.format("%s-%s", "data", LocalDate.now()), response);
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param requestId
   *     : 변경할 포인트 지급 요청의 아이디
   * @param progressType
   *     : 변경할 요청의 상태
   * @return boolean : 요청 성공 여부
   */
  @PutMapping("{requestId}/pointrequest")
  public ApiUtils.ApiResult<Boolean> adminHandleUserPointRequest(@PathVariable("requestId") long requestId,
                                                                 @RequestParam PointRequestProgressType progressType) throws Exception {
    try {
      PointRequest request = pointRequestService.findPointRequestById(requestId).orElseThrow(Exception::new);
      request.setProgressType(progressType);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }

  }

  @PostMapping("/pointrequest")
  public ApiUtils.ApiResult<Boolean> requestPointCalculateByUser(@Valid @RequestBody PointRequestDto pointRequestDto,
                                                                 @LoginUser SessionUser sessionUser) throws Exception {
    try {
      pointRequestService.saveOrUpdate(new PointRequest().of(pointRequestDto));
      return success(Boolean.TRUE);
    } catch (Exception e) {
      if (e instanceof PointCalculateException) {
        throw new PointCalculateException(e.getMessage());
      } else if (e instanceof PointHistoryException) {
        throw new PointHistoryException(e.getMessage());
      } else {
        throw new Exception(e.getMessage());
      }
    }
  }

  @CrossOrigin("*")
  @GetMapping("/user/{email}")
  public ApiUtils.ApiResult<Boolean> findUserByEmail(@Email @PathVariable("email") String email) {
    try {
      return success(userService.findUserByEmail(User.builder().email(email).build()) == null ? Boolean.FALSE : Boolean.TRUE);
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
