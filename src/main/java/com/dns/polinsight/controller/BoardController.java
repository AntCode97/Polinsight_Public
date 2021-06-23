package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@Controller
@Slf4j
//@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping("/boards/new")
  public String createForm(Model model){
    model.addAttribute("boardForm", new BoardForm());
    return "boards/createBoardForm";
  }

  @PostMapping("/boards/new")
  public String create(@Validated BoardForm form, BindingResult result){
    if (result.hasErrors()) {
      return "boards/createBoardForm";
    }
    LocalDateTime registeredAt = LocalDateTime.now();
    Board board = new Board().builder().title(form.getTitle()).searchcontent(form.getContent()).viewcontent(form.getContent()).
            registeredAt(registeredAt).build();
    boardService.saveOrUpdate(board);

    return "redirect:/boards";
  }

  @GetMapping("/boards")
  public String list(Model model){

    return "boards/board";
  }


//  @GetMapping("/board")
//  public ResponseEntity<?> findBoard(@RequestBody Board board) {
//    return ResponseEntity.ok(service.find(board));
//  }
//
//  @GetMapping("/boards")
//  public ResponseEntity<?> findAllBoards() {
//    return ResponseEntity.ok(service.findAll());
//  }
//
//  @PostMapping("/board")
//  public ResponseEntity<?> saveBoard(@RequestBody Board board) {
//    return ResponseEntity.ok(service.saveOrUpdate(board));
//  }
//
//  @PutMapping("/boards")
//  public ResponseEntity<?> updateBoard(@RequestBody Board board) {
//    return ResponseEntity.ok(service.saveOrUpdate(board));
//  }
//
//  @DeleteMapping("/boards")
//  public ResponseEntity<?> deleteBoard(@RequestBody Board board) {
//    service.delete(board);
//    return ResponseEntity.ok(null);
//  }

}
