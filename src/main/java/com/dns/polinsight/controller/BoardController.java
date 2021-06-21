package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;


  @GetMapping("/board")
  public ResponseEntity<?> findBoard(@RequestBody Board board) {
    return ResponseEntity.ok(service.find(board));
  }

  @GetMapping("/boards")
  public ResponseEntity<?> findAllBoards() {
    return ResponseEntity.ok(service.findAll());
  }

  @PostMapping("/board")
  public ResponseEntity<?> saveBoard(@RequestBody Board board) {
    return ResponseEntity.ok(service.saveOrUpdate(board));
  }

  @PutMapping("/boards")
  public ResponseEntity<?> updateBoard(@RequestBody Board board) {
    return ResponseEntity.ok(service.saveOrUpdate(board));
  }

  @DeleteMapping("/boards")
  public ResponseEntity<?> deleteBoard(@RequestBody Board board) {
    service.delete(board);
    return ResponseEntity.ok(null);
  }

}
