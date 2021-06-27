package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.repository.BoardSearch;
import com.dns.polinsight.service.BoardServiceImpl;
import com.dns.polinsight.service.UserServiceImpl;
import com.dns.polinsight.storage.StorageFileNotFoundException;
import com.dns.polinsight.storage.StorageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;


import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
//@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

  private final UserServiceImpl userService;
  private final BoardServiceImpl boardService;
  private final StorageService storageService;


  @GetMapping("/boards/new")
  public String createForm(Model model) throws IOException {
    model.addAttribute("boardForm", new BoardForm());



    return "boards/createBoardForm";
  }



  //TODO: 로그인한 유저 가져와서 넣기
  @PostMapping("/boards/new")
  public String create(BoardForm boardForm, BindingResult result, RedirectAttributes redirectAttributes){
//    System.out.println(boardForm.toString());
//    System.out.println(boardForm.getContent() + boardForm.getTitle());
    if (result.hasErrors()) {
      return "boards/createBoardForm";
    }
    LocalDateTime registeredAt = LocalDateTime.now();


//    List<User> users = userService.findAll();
//    System.out.println("GETNAME" +boardForm.getFile().getName() +"getOriginalFilename" + boardForm.getFile().getOriginalFilename() );

    String viewcontent = boardForm.getContent().replace("\r\n","<br>");
    System.out.println(viewcontent);
    Board board;
    if(!boardForm.getFile().isEmpty()){
      board = new Board().builder().title(boardForm.getTitle()).searchcontent(boardForm.getContent()).viewcontent(viewcontent).
              registeredAt(registeredAt).filePath("./upload-dir/"+boardForm.getFile().getOriginalFilename()).boardType(boardForm.getBoardType()).build();
      storageService.store(boardForm.getFile());
    }else{
      board = new Board().builder().title(boardForm.getTitle()).searchcontent(boardForm.getContent()).viewcontent(viewcontent).
              registeredAt(registeredAt).boardType(boardForm.getBoardType()).build();
    }

    boardService.saveOrUpdate(board);
    redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded " + boardForm.getFile().getOriginalFilename() + "!");

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
  public String search(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable, Model model){
    System.out.println(boardSearch.getSearchType() + boardSearch.getSearchValue() );
    Page<Board> boards;
    if(boardSearch.getSearchType() .equals("TITLE")){
      boards = boardService.searchTitle(boardSearch.getSearchValue(), boardSearch.getBoardType(), pageable);
    } else{
      boards = boardService.searchContent(boardSearch.getSearchValue(),boardSearch.getBoardType(), pageable);
    }

//    List<Board> boards = boardService.findAll();
    model.addAttribute("boards", boards);
    return "/boards/boardList";
  }

  @GetMapping("/boards/{boardId}")
  public String content(@PathVariable("boardId") Long boardId, Model model){
    Board board = boardService.findOne(boardId);
    //파일 리스트 보여줄 때
    model.addAttribute("files", storageService.loadAll().map(
            path -> MvcUriComponentsBuilder.fromMethodName(BoardController.class,
                    "serveFile", path.getFileName().toString()).build().toUri().toString())
            .collect(Collectors.toList()));
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


    try{
      File file = new File(board.getFilePath());
      FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length() , file.getParentFile());
      InputStream input = new FileInputStream(file);
      OutputStream os = fileItem.getOutputStream();
      IOUtils.copy(input, os);
      MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
      boardForm.setFile(multipartFile);
      System.out.println("파일 불러오기 성공" + multipartFile.getOriginalFilename());

    }catch (IOException ex){
      System.out.println(ex);
    }





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


  //파일 클릭했을 때, 다운로드할 수 있게 함
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }



  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
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
