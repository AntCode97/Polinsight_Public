package com.dns.polinsight.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController implements ErrorController {

  public String notFound() {
    return "404";
  }

  public String internalServer() {
    return "404";
  }


}
