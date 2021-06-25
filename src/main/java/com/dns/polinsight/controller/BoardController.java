package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.repository.BoardSearch;
import com.dns.polinsight.service.BoardServiceImpl;
import com.dns.polinsight.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@Slf4j
//@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

  private final UserServiceImpl userService;
  private final BoardServiceImpl boardService;

  @GetMapping("/boards/new")
  public String createForm(Model model){
    model.addAttribute("boardForm", new BoardForm());
    return "boards/createBoardForm";
  }

  //TODO: 로그인한 유저 가져와서 넣기
  @PostMapping("/boards/new")
  public String create(BoardForm boardForm, BindingResult result){
    System.out.println(boardForm.toString());
    System.out.println(boardForm.getContent() + boardForm.getTitle());
    if (result.hasErrors()) {
      return "boards/createBoardForm";
    }
    LocalDateTime registeredAt = LocalDateTime.now();

//    List<User> users = userService.findAll();


    Board board = new Board().builder().title(boardForm.getTitle()).searchcontent(boardForm.getContent()).viewcontent(boardForm.getContent()).
            registeredAt(registeredAt).build();
    boardService.saveOrUpdate(board);

    return "redirect:/boards";
  }

  @GetMapping("/boards")
  public String list(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable, Model model){
    Page<Board> boards = boardService.getBoardList(pageable);
//    List<Board> boards = boardService.findAll();
    model.addAttribute("boards", boards);
    return "/boards/boardList";
  }

  @GetMapping("/boards/search")
  public String search(@RequestParam String searchKind, @RequestParam String searchValue, @ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable, Model model){
    System.out.println(searchKind+ searchValue);
    Page<Board> boards;
    if(searchKind.equals("TITLE")){
      boards = boardService.searchTitle(searchValue, pageable);
    } else{
      boards = boardService.searchContent(searchValue, pageable);
    }

//    List<Board> boards = boardService.findAll();
    model.addAttribute("boards", boards);
    return "/boards/boardList";
  }

  @GetMapping("/boards/{boardId}")
  public String content(@PathVariable("boardId") Long boardId, Model model){
    Board board = boardService.findOne(boardId);
    model.addAttribute("board", board);
    return "/boards/board";
  }
  @GetMapping("/boards/{boardId}/edit")
  public String updateBoardForm(@PathVariable("boardId") Long boardId, Model model){
    Board board = boardService.findOne(boardId);
    BoardForm boardForm = new BoardForm();
    boardForm.setId(board.getId());
    boardForm.setContent(board.getSearchcontent());
    boardForm.setTitle(board.getTitle());
    LocalDateTime registeredAt = LocalDateTime.now();
    boardForm.setRegisteredAt(registeredAt);
    model.addAttribute("boardForm", boardForm);
    return "/boards/updateBoardForm";
  }

  @PostMapping("/boards/{boardId}/edit")
  public String updateBoard(@PathVariable("boardId") Long boardId, @ModelAttribute("boardForm") BoardForm boardForm){
    System.out.println("게시글 수정!" + boardId);
    Board board = boardService.findOne(boardId);
    LocalDateTime registeredAt = LocalDateTime.now();
    boardForm.setRegisteredAt(registeredAt);
    boardService.update(boardId, boardForm);


    return "redirect:/boards/{boardId}";
  }

  @GetMapping("/boards/{boardId}/delete")
  public String delete(@PathVariable("boardId") Long boardId, Model model){
    Board board = boardService.findOne(boardId);
    boardService.delete(board);
    return  "redirect:/boards";
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
