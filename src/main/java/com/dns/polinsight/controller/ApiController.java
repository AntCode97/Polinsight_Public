package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.*;
import com.dns.polinsight.domain.dto.*;
import com.dns.polinsight.exception.*;
import com.dns.polinsight.projection.SurveyMapping;
import com.dns.polinsight.service.*;
import com.dns.polinsight.storage.StorageService;
import com.dns.polinsight.types.*;
import com.dns.polinsight.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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

  private final AttachService attachService;

  private final StorageService storageService;

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
      if (regex.isBlank()) {
        return success(userService.findAllNotInAdmin(pageable));
      } else
        return success(adminService.adminSearchUserByRegex(regex, pageable));
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 모든 설문 반환
   * */
  @GetMapping("/surveys")
  public ApiUtils.ApiResult<Page<SurveyDto>> adminGetAllSurveys(@PageableDefault Pageable pageable,
                                                                @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                                @RequestParam(value = "type", required = false, defaultValue = "ALL") String type) throws Exception {
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ((Sort.by("id").ascending().and(Sort.by("endAt").descending()))));
    Page<SurveyMapping> surveyList;
    try {
      if (type.isBlank() || type.equals("ALL")) {
        if (regex.isBlank()) {
          surveyList = surveyService.findAll(pageable);
        } else {
          surveyList = surveyService.findAllAndRegex(pageable, regex);
        }
      } else if (type.equals("INDEX")) {
        surveyList = surveyService.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("progress").descending().and((Sort.by("endAt").ascending().and(Sort.by("id"))))));
      } else {
        type = type.toUpperCase();
        if (regex.isBlank()) {
          surveyList = surveyService.findAllByTypes(pageable, ProgressType.valueOf(type));
        } else {
          surveyList = surveyService.findAllByTypesAndRegex(pageable, ProgressType.valueOf(type), regex);
        }
      }

      return success(new PageImpl<>(surveyList.getContent().stream().map(SurveyDto::of).collect(Collectors.toList()), pageable, surveyList.getTotalElements()));
    } catch (Exception e) {
      log.error(e.getMessage());
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
      throw new Exception();
    }
  }

  /*
   * 저장된 설문 목록 삭제를 위한 api
   * */
  @DeleteMapping("/survey")
  public ApiUtils.ApiResult<Boolean> adminDeleteSurveyById(@RequestBody Survey survey) throws Exception {
    try {
      log.info("Deleted Survey ID : {}, Title : {}", survey.getId(), survey.getTitle());
      surveyService.deleteSurveyById(survey.getId());
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 저장된 서베이 목록 수정을 api
   * */
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional
  @PutMapping("/survey")
  public ApiUtils.ApiResult<Boolean> adminUpdateSurveyById(@RequestBody SurveyDto dto) throws Exception {
    try {
      Survey survey = surveyService.findById(dto.getId()).orElseThrow();
      survey.updateInfo(dto);
      surveyService.update(survey);
      log.info("Survey Updated ID: {}, Title : {}", survey.getId(), survey.getTitle());
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional
  @PostMapping("/survey/thumbnail/{surveyId}")
  public ApiUtils.ApiResult<Boolean> adminUpdateSurveyThumbnail(MultipartFile thumbnail,
                                                                @PathVariable("surveyId") Long surveyId) throws Exception {
    try {
      Survey survey = surveyService.findById(surveyId).orElseThrow();
      UUID uuid = UUID.randomUUID();
      String thumbnailPath = storageService.saveThumbnail(uuid.toString(), thumbnail);
      storageService.store(uuid.toString(), thumbnail);
      survey.setThumbnail(thumbnailPath);
      survey.setOriginalName(thumbnail.getOriginalFilename());
      surveyService.update(survey);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /*
   * 사용자가 참여한 서베이 목록 가져옴
   * */
  @GetMapping("participatelist")
  public ApiUtils.ApiResult<List<ParticipateSurveyDto>> getUserParticipateSurvey(@CurrentUser User user) throws Exception, WrongAccessException {
    try {
      HashMap<Long, String> map = new HashMap<>();
      for (var sv : surveyService.findAll())
        map.put(sv.getId(), sv.getTitle());
      return success(participateSurveyService.findAllByUserId(user.getId())
                                             .parallelStream()
                                             .map(ps ->
                                                 ParticipateSurveyDto.of(ps, map.get(ps.getSurveyId()))
                                             )
                                             .collect(Collectors.toList()));
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

  @PreAuthorize("isAuthenticated()")
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
                                      .name(user.getName())
                                      .build();

      pointRequestService.saveOrUpdate(preq);
      userService.subUserPoint(user.getId(), pointRequestDto.getPoint());
      pointHistoryService.saveOrUpdate(PointHistory.builder()
                                                   .amount(pointRequestDto.getPoint())
                                                   .content("포인트 정산 요청")
                                                   .total(user.getPoint() - pointRequestDto.getPoint())
                                                   .sign(false)
                                                   //                                                   .userId(user.getId())
                                                   .user(user)
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


  @GetMapping("/points")
  public ApiUtils.ApiResult<Page<PointRequest>> getAllPointRequests(@PageableDefault Pageable pageable,
                                                                    @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                                    @RequestParam(value = "type", required = false, defaultValue = "ALL") String type) throws Exception {
    try {
      if (regex.isBlank()) {
        if (type.equalsIgnoreCase("ALL")) {
          return success(pointRequestService.findAllPointRequests(pageable));
        } else if (type.equalsIgnoreCase("REQUESTED")) {
          return success(pointRequestService.findAllOngoingRequest(pageable));
        } else {
          return success(pointRequestService.findAllPointRequestsAndType(pageable, PointRequestProgressType.valueOf(type.toUpperCase())));
        }
      } else {
        if (type.equalsIgnoreCase("ALL")) {
          return success(pointRequestService.findAllPointRequestsByRegex(pageable, regex));
        } else if (type.equalsIgnoreCase("REQUESTED")) {
          return success(pointRequestService.findAllOngoingRequestByRegex(pageable, regex));
        } else {
          return success(pointRequestService.findAllPointRequestsByRegexAndType(pageable, regex, PointRequestProgressType.valueOf(type.toUpperCase())));
        }
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PutMapping("/points/{id}")
  public ApiUtils.ApiResult<Boolean> updateUserRequestByAdmin(@PathVariable("id") long pointReqId,
                                                              @RequestBody PointRequestDto dto) throws Exception {
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


  @PermitAll
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
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }


  @Transactional
  @GetMapping("posts")
  public ApiUtils.ApiResult<Page<com.dns.polinsight.projection.PostMapping>> findPostByTypes(@RequestParam(value = "type") String type,
                                                                                             @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                                                             @PageableDefault Pageable pageable) throws Exception {

    try {
      if (regex.isBlank()) {
        return success(postService.apiFindPostsByType(PostType.valueOf(type.toUpperCase(Locale.ROOT)), pageable));
      } else {
        return success(postService.findAllByTypesAndRegex(PostType.valueOf(type.toUpperCase(Locale.ROOT)), regex, pageable));
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/participates/{surveyId}")
  public ApiUtils.ApiResult<Boolean> isExistParticipates(@CurrentUser User user,
                                                         @PathVariable("surveyId") Long surveyId) {
    return success(participateSurveyService.isExistParticipates(surveyId));
  }

  @Transactional
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/insight/thumbnail/{postId}")
  public ApiUtils.ApiResult<Boolean> uploadPolsinsightImageThumbnail(MultipartFile thumbnail,
                                                                     @PathVariable("postId") Long postId) throws Exception {
    try {
      Post post = postService.findOne(postId);
      UUID uuid = UUID.randomUUID();
      String thumbnailPath = storageService.saveThumbnail(uuid.toString(), thumbnail);
      post.setThumbnail(thumbnailPath);
      postService.updatePost(post);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @GetMapping("/post/{postId}")
  public ApiUtils.ApiResult<PostDTO> getPostByPostId(@PathVariable("postId") Long postId) {
    Post post = postService.findOne(postId);
    return success(PostDTO.of(post));
  }

}
