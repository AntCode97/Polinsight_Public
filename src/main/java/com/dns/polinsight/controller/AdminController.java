package com.dns.polinsight.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

  @GetMapping("/admin")
  public ModelAndView goAdminPage() {
    return new ModelAndView("admin");
  }


}
