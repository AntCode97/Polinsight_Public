package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.*;
import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.PointCalculateException;
import com.dns.polinsight.exception.PointHistoryException;
import com.dns.polinsight.exception.UnAuthorizedException;
import com.dns.polinsight.service.*;
import com.dns.polinsight.types.PointRequestProgressType;
import com.dns.polinsight.types.UserRoleType;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.ExcelUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  private final PostService postService;

  private final CollectorService collectorService;

  private PointHistoryService pointHistoryService;

  @PutMapping("{postId}/count")
  public ApiUtils.ApiResult<Boolean> handlePostCount(@PathVariable long postId) throws Exception {
    try {
      Post post = postService.findOne(postId);
      post.setViewcnt(post.getViewcnt() + 1);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/surveys/sync")
  public ApiUtils.ApiResult<List<Survey>> surveySyncWithSM() throws Exception {
    try {
      return success(surveyService.getSurveyListAndSyncPerHour());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/user/find/{regex}")
  public ApiUtils.ApiResult<List<UserDto>> adminUserFind(
      @PageableDefault Pageable pageable,
      @PathVariable(name = "regex") String regex) throws Exception {
    try {
      return success(adminService.adminSerchUserByRegex(regex, pageable).stream().map(UserDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/user/find/{regex}/total")
  public ApiUtils.ApiResult<Long> adminCountUserFind(
      @PathVariable(name = "regex") String regex) throws Exception {
    try {
      return success(adminService.countUserFindRegex(regex));
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
  public ApiUtils.ApiResult<List<UserDto>> adminFindAllUsers(@PageableDefault Pageable pageable) throws Exception {
    try {
      return success(userService.findAll(pageable).getContent().parallelStream().filter(user -> !user.getRole().equals(UserRoleType.ADMIN)).map(UserDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 모든 설문 반환
   * */
  @GetMapping("/surveys")
  public ApiUtils.ApiResult<List<Survey>> adminGetAllSurveys(@PageableDefault Pageable pageable,
                                                             @RequestParam(value = "type", required = false) String type) throws Exception {
    try {
      List<Survey> surveyList = null;
      if (type != null && type.equals("index")) {
        List<Survey> list = surveyService.findAll();
        list.stream().filter(obj -> LocalDateTime.now().isBefore(obj.getEndAt())).collect(Collectors.toList()).sort((o1, o2) -> {
              if (o1.getEndAt().compareTo(o2.getEndAt()) == 0) {
                return o1.getStatus().getProgress().compareTo(o2.getStatus().getProgress());
              } else
                return o1.getEndAt().compareTo(o2.getEndAt());
            }
        );
        surveyList = list;
      }
      if (surveyList == null)
        surveyList = surveyService.findAll(pageable).getContent();

      return success(surveyList);
    } catch (Exception e) {
      throw new Exception();
    }
  }


  @GetMapping("/survey/total")
  public ApiUtils.ApiResult<Long> adminCountAllSurveys() throws Exception {
    try {
      return success(surveyService.countAllSurvey());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 정규식을 통한 다건 설문 검색
   * */
  @GetMapping("/surveys/{regex}")
  public ApiUtils.ApiResult<List<Survey>> adminGetSurveyByRegex(@PathVariable(name = "regex") String regex, @PageableDefault Pageable pageable) throws Exception {
    try {
      return success(surveyService.findSurveysByTitleRegex(regex, pageable));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/survey/find/{regex}/total")
  public ApiUtils.ApiResult<Long> adminCountSurveyFindByRegex(
      @PathVariable(name = "regex") String regex) throws Exception {
    try {
      return success(adminService.countUserFindRegex(regex));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 설문 목록 삭제를 위한 api
   * */
  @DeleteMapping("/survey")
  public ApiUtils.ApiResult<Boolean> adminDeleteSurveyById(@RequestBody Survey survey) throws Exception {
    try {
      surveyService.deleteSurveyById(survey.getId());
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
  public ApiUtils.ApiResult<List<ParticipateSurvey>> getUserParticipateSurvey(@LoginUser SessionUser sessionUser) throws Exception {
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
      request.setProgress(progressType);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }

  }

  @Transactional
  @PostMapping("/pointrequest")
  public ApiUtils.ApiResult<Boolean> requestPointCalculateByUser(@Valid @RequestBody PointRequestDto pointRequestDto,
                                                                 @LoginUser SessionUser sessionUser) throws Exception {
    if (sessionUser == null)
      throw new UnAuthorizedException("Unauthorized error");

    try {
      PointRequest preq = PointRequest.builder()
                                      .email(sessionUser.getEmail())
                                      .requestPoint(pointRequestDto.getPoint())
                                      .account(pointRequestDto.getAccount())
                                      .requestedAt(LocalDateTime.now())
                                      .progress(PointRequestProgressType.REQUESTED)
                                      .bank(pointRequestDto.getBank())
                                      .uid(sessionUser.getId())
                                      .build();

      pointRequestService.saveOrUpdate(preq);
      userService.subUserPoint(sessionUser.getId(), pointRequestDto.getPoint());
      pointHistoryService.saveOrUpdate(PointHistory.builder()
                                                   .amount(pointRequestDto.getPoint())
                                                   .total(sessionUser.getPoint() - pointRequestDto.getPoint())
                                                   .sign(false)
                                                   .userId(sessionUser.getId())
                                                   .build());
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping("/user/{email}")
  public ApiUtils.ApiResult<Boolean> isExistEmail(@Email @PathVariable("email") String email) throws NotFoundException {
    try {
      return success(!userService.isExistEmail(email));
    } catch (RuntimeException e) {
      throw new NotFoundException("Email Number Not Found");
    }
  }

  @GetMapping("/user/recommend/{phone}")
  public ApiUtils.ApiResult<Boolean> isExistPhoneForRecommend(@PathVariable("phone") String recommendPhone) throws NotFoundException {
    try {
      return success(!userService.isExistPhone(recommendPhone));
    } catch (Exception e) {
      throw new NotFoundException("Phone Number Not Found");
    }
  }

  @GetMapping("/points/total")
  public ApiUtils.ApiResult<Long> countPointRequests() throws Exception {
    try {
      return success(pointRequestService.countAllPointRequests());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/points")
  public ApiUtils.ApiResult<List<PointRequestDto>> getAllPointRequests(@PageableDefault Pageable pageable) throws Exception {
    try {
      return success(pointRequestService.getAllPointRequests(pageable).stream().map(PointRequestDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/points/{regex}")
  public ApiUtils.ApiResult<List<PointRequestDto>> getAllPointRequests(@PageableDefault Pageable pageable,
                                                                       @PathVariable("regex") String regex) throws Exception {
    try {
      return success(pointRequestService.getAllPointRequestsByRegex(pageable, regex).stream().map(PointRequestDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/points/{regex}/total")
  public ApiUtils.ApiResult<Long> countPointRequests(@PathVariable("regex") String regex) throws Exception {
    try {
      return success(pointRequestService.countPointRequestsByRegex(regex));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PutMapping("/points/{id}")
  public ApiUtils.ApiResult<Boolean> updateUserRequestByAdmin(@PathVariable("id") long pointReqId, @RequestBody PointRequestDto dto) throws Exception {
    try {
      PointRequest pointRequest = pointRequestService.findPointRequestById(pointReqId).orElseThrow();
      pointRequest.progressUpdate(dto);
      pointRequestService.saveOrUpdate(pointRequest);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @DeleteMapping("/points/{id}")
  public ApiUtils.ApiResult<Boolean> deleteUserRequestByAdmin(@PathVariable("id") long pointReqId) throws Exception {
    try {
      pointRequestService.deletePointRequestById(pointReqId);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PutMapping("/admin/survey")
  public ApiUtils.ApiResult<Boolean> adminUpdateSurvey(@RequestBody Map<String, String> map) throws Exception {
    try {
      surveyService.adminSurveyUpdate(
          Long.parseLong(map.get("id")),
          Long.parseLong(map.get("point")),
          map.get("create"),
          map.get("end"),
          map.get("progress"));
      return success(true);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
