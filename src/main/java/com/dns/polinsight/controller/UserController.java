package com.dns.polinsight.controller;

import com.dns.polinsight.service.UserServlce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserServlce servlce;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

}
