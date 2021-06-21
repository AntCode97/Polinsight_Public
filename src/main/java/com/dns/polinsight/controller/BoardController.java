package com.dns.polinsight.controller;

import com.dns.polinsight.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;

}
