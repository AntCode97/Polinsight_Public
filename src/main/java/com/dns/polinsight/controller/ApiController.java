package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.*;
import com.dns.polinsight.domain.dto.ParticipateSurveyDto;
import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.domain.dto.SurveyDto;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.exception.*;
import com.dns.polinsight.mapper.SurveyMapping;
import com.dns.polinsight.service.*;
import com.dns.polinsight.types.*;
import com.dns.polinsight.utils.ApiUtils;
import com.dns.polinsight.utils.ExcelUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
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

  private final PointHistoryService pointHistoryService;

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

  @DeleteMapping("/user/{userId}")
  @Transactional
  public ApiUtils.ApiResult<Boolean> adminUserDeleteByEmail(@PathVariable("userId") Long userId) throws Exception {
    try {
      userService.deleteUserById(userId);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 회원 가입된 모든 유저 정보 반환
   * */
  @GetMapping("/users")
  public ApiUtils.ApiResult<Page<UserDto>> adminFindAllUsers(@PageableDefault Pageable pageable,
                                                             @RequestParam(value = "regex", required = false, defaultValue = "") String regex) throws Exception {
    try {
      if (regex.isBlank() || regex.isEmpty()) {
        return success(userService.findAllNotInAdmin(pageable));
      } else
        return success(adminService.adminSerchUserByRegex(regex, pageable));
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 모든 설문 반환
   * */
  @GetMapping("/surveys")
  public ApiUtils.ApiResult<Page<SurveyMapping>> adminGetAllSurveys(@PageableDefault Pageable pageable,
                                                                    @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                                    @RequestParam(value = "type", required = false, defaultValue = "ALL") String type) throws Exception {
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("status.progress").and((Sort.by("endAt").ascending().and(Sort.by("id")))));
    try {
      if (type.isBlank() || type.equals("ALL")) {
        if (regex.isBlank()) {
          return success(surveyService.findAll(pageable));
        } else {
          return success(surveyService.findAllAndRegex(pageable, regex));
        }
      } else if (type.equals("INDEX")) {
        // TODO: 2021-08-28 정렬 다시 하기
        Page<SurveyMapping> page = surveyService.findAll(pageable);
        log.warn(page.getContent().get(0).toString());
        List<SurveyMapping> list = page.getContent().parallelStream().sorted((o1, o2) -> {
          int progressComp = o1.getProgress().compareTo(o2.getProgress());
          if (progressComp < 0)
            return -1;
          else if (progressComp == 0) {
            if (!o1.getEnd().isBlank() && !o2.getEnd().isBlank()) {
              int endAtComp = LocalDate.parse(o1.getEnd()).compareTo(LocalDate.parse(o2.getEnd()));
              if (endAtComp > 0)
                return -1;
              else if (endAtComp == 0)
                return o1.getId().compareTo(o2.getId());
            } else {
              return o1.getId().compareTo(o2.getId());
            }
          }
          return 1;
        }).collect(Collectors.toList());
        return success(new PageImpl<>(list, pageable, page.getTotalElements()));
      } else {
        type = type.toUpperCase();
        if (regex.isBlank()) {
          return success(surveyService.findAllByTypes(pageable, ProgressType.valueOf(type)));
        } else {
          return success(surveyService.findAllByTypesAndRegex(pageable, ProgressType.valueOf(type), regex));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception();
    }
  }

  @GetMapping("/surveys/notAdmin")
  public ApiUtils.ApiResult<Page<SurveyMapping>> getAllSurvey(@PageableDefault Pageable pageable,
                                                              @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                              @RequestParam(value = "type", required = false, defaultValue = "ALL") String type) throws Exception {
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("progress").and((Sort.by("endAt").ascending().and(Sort.by("id")))));
    try {
      if (type == null || type.equals("ALL") || type.equals("INDEX")) {
        if (regex.isBlank()) {
          return success(surveyService.findAllSurveysByProgressTypeNotLike(pageable, ProgressType.BEFORE));
        } else {
          return success(surveyService.findAllAndRegex(pageable, regex));
        }
      } else {
        if (regex.isBlank()) {
          return success(surveyService.findAllByTypes(pageable, ProgressType.valueOf(type)));
        } else {
          return success(surveyService.findAllByTypesAndRegex(pageable, ProgressType.valueOf(type), regex));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception();
    }
  }

  /*
   * 정규식을 통한 다건 설문 검색
   * */
  @GetMapping("/surveys/{regex}")
  public ApiUtils.ApiResult<List<SurveyDto>> adminGetSurveyByRegex(@PathVariable(name = "regex") String regex, @PageableDefault Pageable pageable) throws Exception {
    try {
      return success(surveyService.findSurveysByTitleRegex(regex, pageable).parallelStream().map(SurveyDto::new).collect(Collectors.toList()));
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
  public ApiUtils.ApiResult<Boolean> adminUpdateSurveyById(Survey survey) throws Exception {
    try {
      surveyService.update(survey);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 사용자가 참여한 서베이 목록 가져옴
   * */
  @GetMapping("participate")
  public ApiUtils.ApiResult<List<ParticipateSurveyDto>> getUserParticipateSurvey(@CurrentUser User user) throws Exception, WrongAccessException {
    try {
      return success(participateSurveyService.findAllByUserId(user.getId()).parallelStream().map(ParticipateSurveyDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new WrongAccessException(e.getMessage());
    }
  }

  @GetMapping("{userId}/pointrequest")
  public ApiUtils.ApiResult<List<PointRequestDto>> getAllRequestOfUser(@CurrentUser User user) throws Exception {
    try {
      return success(pointRequestService.getUserPointRequests(user.getId()).stream().map(PointRequestDto::new).collect(Collectors.toList()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/points/excel/{userId}")
  public void getExcelFromAllPointRequests(HttpServletResponse response,
                                           @PathVariable(value = "userId", required = false) Long userId) {
    try {
      ExcelUtil<PointRequest> excelUtil = new ExcelUtil<>();
      if (userId == 0) {
        excelUtil.createExcelToResponse(pointRequestService.findAll(), String.format("%s-%s", "point_request", LocalDate.now()), response);
      } else {
        excelUtil.createExcelToResponse(pointRequestService.getUserPointRequests(userId), String.format("%s-%s", "point_request", LocalDate.now()), response);
      }
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/pointhistory/excel")
  public void getExcelFromAllHistories(HttpServletResponse response,
                                       @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId) {
    try {
      ExcelUtil<PointHistory> excelUtil = new ExcelUtil<>();
      if (userId == 0) {
        excelUtil.createExcelToResponse(pointHistoryService.findAll(), String.format("%s-%s", "point_history", LocalDate.now()), response);
      } else {
        excelUtil.createExcelToResponse(pointHistoryService.findAllPointHistoryByUserId(userId), String.format("%s-%s", "point_history", LocalDate.now()), response);
      }
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/participates/excel")
  public void getExcelFromAllParticipates(HttpServletResponse response,
                                          @RequestParam(value = "userId", required = false, defaultValue = "-1") Long userId) {
    try {
      ExcelUtil<ParticipateSurvey> excelUtil = new ExcelUtil<>();
      if (userId == -1) {
        excelUtil.createExcelToResponse(participateSurveyService.findAll(), String.format("%s-%s", "participate_survey", LocalDate.now()), response);
      } else {
        excelUtil.createExcelToResponse(participateSurveyService.findAllByUserId(userId), String.format("%s-%s", "participate_survey", LocalDate.now()), response);
      }
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param requestId
   *     : 변경할 포인트 지급 요청의 아이디
   * @param progressType
   *     : 변경할 요청의 상태
   *
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
                                                                 @CurrentUser User user) throws Exception {
    if (user == null)
      throw new UnAuthorizedException("Unauthorized error");

    try {
      PointRequest preq = PointRequest.builder()
                                      .email(user.getEmail())
                                      .requestPoint(pointRequestDto.getPoint())
                                      .account(pointRequestDto.getAccount())
                                      .requestedAt(LocalDateTime.now())
                                      .progress(PointRequestProgressType.REQUESTED)
                                      .bank(pointRequestDto.getBank())
                                      .uid(user.getId())
                                      .build();

      pointRequestService.saveOrUpdate(preq);
      userService.subUserPoint(user.getId(), pointRequestDto.getPoint());
      pointHistoryService.saveOrUpdate(PointHistory.builder()
                                                   .amount(pointRequestDto.getPoint())
                                                   .content("포인트 정산 요청")
                                                   .total(user.getPoint() - pointRequestDto.getPoint())
                                                   .sign(false)
                                                   .userId(user.getId())
                                                   .requestedAt(pointRequestDto.getRequestedAt())
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
  public ApiUtils.ApiResult<Boolean> isExistEmail(@PathVariable("email") Email email) throws NotFoundException {
    try {
      return success(!userService.isExistEmail(email));
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new NotFoundException("Email Number Not Found");
    }
  }

  @GetMapping("/user/recommend/{phone}")
  public ApiUtils.ApiResult<Boolean> isExistPhoneForRecommend(@PathVariable("phone") Phone recommendPhone) throws NotFoundException {
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
  public ApiUtils.ApiResult<Page<PointRequestDto>> getAllPointRequests(@PageableDefault Pageable pageable) throws Exception {
    try {
      return success(pointRequestService.getAllPointRequests(pageable));
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

  @PostMapping("/find/email")
  public ApiUtils.ApiResult<Email> findEmail(@RequestBody UserDto userDto) throws Exception {
    try {
      return success(userService.findUserEmailByNameAndPhone(userDto.getName(), Phone.of(userDto.getPhone())).orElseThrow(UserNotFoundException::new).getEmail());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/pointhistories/{userid}")
  public ApiUtils.ApiResult<List<PointHistory>> findAllPointHistoryByUserId(@PathVariable("userid") long userId, @PageableDefault Pageable pageable) throws Exception {
    try {
      return success(pointHistoryService.findAllPointHistoryByUserId(userId, pageable));
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("posts")
  public ApiUtils.ApiResult<Page<com.dns.polinsight.mapper.PostMapping>> fidnPostByTypes(@RequestParam(value = "type") String type,
                                                                                         @PageableDefault Pageable pageable) throws Exception {
    try {
      return success(postService.findPostsByType(PostType.valueOf(type.toUpperCase(Locale.ROOT)), pageable));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
