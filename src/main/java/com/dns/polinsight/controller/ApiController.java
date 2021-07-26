package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.*;
import com.dns.polinsight.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

  private final SurveyQueryService pointService;

  private final UserService userService;

  private final SurveyService surveyService;

  private final AdminService adminService;

  private final ParticipateSurveyService participateSurveyService;

  @GetMapping("/surveys/sync")
  public ResponseEntity<Map<String, Object>> surveySyncWithSM() {
    Map<String, Object> map = new HashMap<>();
    try {
      // FIXME: 2021/07/26 : embedded 한 데이터가 넘어오지 않음
      map.put("data", surveyService.getSurveyListAndSyncPerHour());
      map.put("code", 200);
      map.put("msg", "sync and save success");
      map.put("error", null);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("data", null);
      map.put("error", e.getMessage());
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("/user/find/{regex}")
  public ResponseEntity<Map<String, Object>> adminUserFind(@PathVariable(name = "regex") String regex) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("data", adminService.adminSerchUserByRegex(regex));
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @DeleteMapping("/user/{email}")
  public ResponseEntity<Map<String, Object>> adminUserDeleteByEmail(@PathVariable(name = "email") String email) {
    Map<String, Object> map = new HashMap<>();
    try {
      userService.deleteUserByEmail(email);
      map.put("data", true);
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("/users")
  public ResponseEntity<Map<String, Object>> adminFindAllUsers() {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("data", userService.findAll());
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("/survyes")
  public ResponseEntity<Map<String, Object>> adminGetAllSurveys() {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("data", surveyService.findAll());
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("/survey/{regex}")
  public ResponseEntity<Map<String, Object>> adminGetSurveyByRegex(@PathVariable(name = "regex") String regex, String type /*검색 타입*/) {
    Map<String, Object> map = new HashMap<>();
    try {
      if (type.equals("title")) {
        map.put("data", surveyService.findSurveysByTitleRegex(regex));
      } else {
        map.put("data", surveyService.findSurveysByEndDate(LocalDateTime.parse(regex)));
      }
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @DeleteMapping("/survey/{id}")
  public ResponseEntity<Map<String, Object>> adminDeleteSurveyById(@PathVariable(name = "id") Long surveyId) {
    Map<String, Object> map = new HashMap<>();
    try {
      surveyService.deleteSurveyById(surveyId);
      map.put("data", true);
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @PutMapping("/survey")
  public ResponseEntity<Map<String, Object>> adminUpdateSurveyById(Survey survey) {
    Map<String, Object> map = new HashMap<>();
    try {
      surveyService.update(survey);
      map.put("data", surveyService.findAll());
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
      e.printStackTrace();
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("participate")
  public ResponseEntity<Map<String, Object>> getUserParticipateSurvey(@LoginUser SessionUser sessionUser, @RequestParam("type") String type) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("data", participateSurveyService.findByUserId(sessionUser.getId()));
      map.put("error", null);
    } catch (Exception e) {
      map.put("data", null);
      map.put("error", e.getMessage());
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("{id}/pointrequestlist")
  public void getExcelFromAllRequests(HttpServletResponse response,
                                      @PathVariable("id") long userId,
                                      @RequestBody(required = false) PointRequest pointRequest) {
    try {
      ExcelUtil<PointRequest> excelUtil = new ExcelUtil<>();
      excelUtil.createExcelToResponse(pointService.getUserPointRequests(userId), String.format("%s-%s", "data", LocalDate.now()), response);
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
    }
  }

}
