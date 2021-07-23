package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.service.AdminService;
import com.dns.polinsight.service.SurveyService;
import com.dns.polinsight.service.UserService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

  private final UserService userService;

  private final SurveyService surveyService;

  private final AdminService adminService;

  @GetMapping("/surveys/sync")
  public ResponseEntity<Map<String, Object>> surveySyncWithSM() {
    Map<String, Object> map = new HashMap<>();
    try {
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
  public ResponseEntity<Map<String, Object>> adminGetSurveyByRegex(@PathVariable(name = "regex") String regex) {
    Map<String, Object> map = new HashMap<>();
    try {
      map.put("data", surveyService.findSurveyByRgex(regex));
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

}
