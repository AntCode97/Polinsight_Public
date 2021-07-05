package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.*;
import com.dns.polinsight.repository.BoardSearch;
import com.dns.polinsight.service.AttachService;
import com.dns.polinsight.service.BoardService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.storage.StorageFileNotFoundException;
import com.dns.polinsight.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;


@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

  private final UserService userService;

  private final BoardService boardService;

  private final StorageService storageService;

  private final AttachService attachService;


  @GetMapping("/boards/new")
  public String createForm(Model model, @LoginUser SessionUser user) throws IOException {
    model.addAttribute("boardDTO", new BoardDTO());
    if (user != null && user.getRole() == UserRole.ADMIN) {
      //      model.addAttribute("user", user);
      return "boards/createBoardForm";
    }
    return "index";
  }


  @PostMapping("/boards/new")
  public String create(BoardDTO boardDTO, BindingResult result, RedirectAttributes redirectAttributes, @LoginUser SessionUser user) {
    log.info("Result: " + result + ", data: " + boardDTO.toString());
    // NOTE 2021-07-04 0004 : BindingResult??
    if (result.hasErrors()) {
      return "boards/createBoardForm";
    }

    boardDTO.transViewcontent();

    // NOTE 2021-07-04 0004 : 관리자 확인 로직 추가
    if (user != null && user.getRole() == UserRole.ADMIN) {
      //TODO: 관리자 역활인지 확인하는 로직 추가해야함
      User admin = userService.findUserByEmail(User.builder().email(user.getEmail()).build());
      boardDTO.setUser(admin);
      boardDTO.setRegisteredAt(LocalDateTime.now());
      Board board = boardService.addBoard(boardDTO);
      boardDTO.setId(board.getId());
      attachService.addAttach(boardDTO);
    }

    redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + boardDTO.getFiles() + "!");

    return "redirect:/boards";
  }


  @GetMapping("/boards")
  public String list(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable, Model model) {
    Page<Board> boards = boardService.getBoardList(pageable);
    //    List<Board> boards = boardService.findAll();
    boardService.renewBoard();
    model.addAttribute("boards", boards);
    return "/boards/boardList";
  }

  @GetMapping("/boards/search")
  public String search(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable, Model model) {
    //    System.out.println(boardSearch.getSearchType() + boardSearch.getSearchValue());
    Page<Board> boards;
    if (boardSearch.getSearchType() == SearchType.TITLE) {
      boards = boardService.searchTitle(boardSearch.getSearchValue(), boardSearch.getBoardType(), pageable);
    } else {
      boards = boardService.searchContent(boardSearch.getSearchValue(), boardSearch.getBoardType(), pageable);
    }
    model.addAttribute("boards", boards);
    return "/boards/boardList";
  }

  @GetMapping("/boards/{boardId}")
  public String content(@PathVariable("boardId") Long boardId, Model model, @LoginUser SessionUser user) {
    //파일 리스트 보여줄 때
    //    model.addAttribute("files", storageService.loadAll().map(
    //            path -> MvcUriComponentsBuilder.fromMethodName(BoardController.class,
    //                    "serveFile", path.getFileName().toString()).build().toUri().toString())
    //            .collect(Collectors.toList()));
    model.addAttribute("files", attachService.findFiles(boardId));
    //    if (user != null) {
    //      model.addAttribute("user", user);
    //    }
    model.addAttribute("board", boardService.findOne(boardId));
    return "/boards/board";
  }

  @GetMapping("/boards/{boardId}/edit")
  public String updateBoard(@PathVariable("boardId") Long boardId, Model model, @LoginUser SessionUser user) {
    Board board = boardService.findOne(boardId);
    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setId(board.getId());
    boardDTO.setContent(board.getSearchcontent());
    boardDTO.setViewcontent(board.getViewcontent());
    boardDTO.setAttaches(board.getAttaches());
    boardDTO.setUser(board.getUser());
    boardDTO.setTitle(board.getTitle());
    LocalDateTime registeredAt = LocalDateTime.now();
    boardDTO.setRegisteredAt(registeredAt);

    model.addAttribute("files", attachService.findFiles(boardId));

    //    if (user != null) {
    //      model.addAttribute("user", user);
    //    }

    //    try{
    //      File file = new File(board.getFilePath());
    //      FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length() , file.getParentFile());
    //      InputStream input = new FileInputStream(file);
    //      OutputStream os = fileItem.getOutputStream();
    //      IOUtils.copy(input, os);
    //      MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
    //      boardDTO.setFile(multipartFile);
    //      System.out.println("파일 불러오기 성공" + multipartFile.getOriginalFilename());
    //
    //    }catch (IOException ex){
    //      System.out.println(ex);
    //    }
    //


    model.addAttribute("boardDTO", boardDTO);
    return "/boards/updateBoardForm";
  }

  @PostMapping("/boards/{boardId}/edit")
  public String updateBoard(@PathVariable("boardId") Long boardId, @ModelAttribute("boardDTO") BoardDTO boardDTO, @LoginUser SessionUser user) {
    //    System.out.println("게시글 수정!" + boardId);
    User admin = userService.findUserByEmail(User.builder().email(user.getEmail()).build());
    boardDTO.setUser(admin);
    boardDTO.setId(boardId);
    boardDTO.setRegisteredAt(LocalDateTime.now());
    boardDTO.transViewcontent();

    boardService.addBoard(boardDTO);
    attachService.addAttach(boardDTO);

    return "redirect:/boards/{boardId}";
  }

  @GetMapping("/boards/{boardId}/delete")
  public String delete(@PathVariable("boardId") Long boardId, Model model) {
    Board board = boardService.findOne(boardId);
    attachService.deleteAttaches(boardId);
    boardService.delete(board);
    return "redirect:/boards";
  }


  //파일 클릭했을 때, 다운로드할 수 있게 함
  @GetMapping("/boards/upload-dir/{filename:.+}")
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


  @GetMapping("/boards/{boardId}/{file}/delete")
  public String deleteFile(@PathVariable("boardId") Long boardId, @PathVariable("file") String filename, Model model) {
    attachService.delete(attachService.findByname(filename).get(0));
    return "redirect:/boards/" + boardId + "/edit";
  }

}
