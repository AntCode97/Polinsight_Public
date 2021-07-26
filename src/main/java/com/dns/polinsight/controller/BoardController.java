package com.dns.polinsight.controller;

import com.dns.polinsight.config.oauth.LoginUser;
import com.dns.polinsight.config.oauth.SessionUser;
import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardDTO;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.repository.BoardSearch;
import com.dns.polinsight.service.AttachService;
import com.dns.polinsight.service.BoardService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.storage.StorageFileNotFoundException;
import com.dns.polinsight.storage.StorageService;
import com.dns.polinsight.types.BoardType;
import com.dns.polinsight.types.SearchType;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

  private final UserService userService;

  private final BoardService boardService;

  private final StorageService storageService;

  private final AttachService attachService;


  @GetMapping("admin2/boards")
  public String adminBoardList(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable, Model model, @RequestParam Map<String, Object> paramMap ) {

    Page<Board> boards;
    if(paramMap.get("keyword") != null){
      String keyword = paramMap.get("keyword").toString();
      System.out.println(keyword);
      model.addAttribute("keyword", keyword);
      boards = boardService.searchKeyword(keyword, pageable);
    } else{
      boards = boardService.getBoardList(pageable);
    }

    //    List<Board> boards = boardService.findAll();
    boardService.renewBoard();

    model.addAttribute("boards", boards);
    if(boardSearch.getBoardType() !=null){
      model.addAttribute("boardSearch", boardSearch);
    }
    return "admin/boards";
  }




  @GetMapping("admin2/boards/new")
  public String adminCreateForm(Model model, @LoginUser SessionUser user) throws IOException {
    model.addAttribute("boardDTO", new BoardDTO());
//    if (user != null && user.getRole() == UserRoleType.ADMIN) {
//      //      model.addAttribute("user", user);
//      return "boards/createBoardForm";
//    }
//    return "index";
    //로그인이 안되서 일단 이렇게 진행
    return "admin/admin_board_register";
  }


  @PostMapping("admin2/boards/new")
  public String adminCreate(BoardDTO boardDTO, BindingResult result, RedirectAttributes redirectAttributes, @LoginUser SessionUser user, MultipartFile[] file) {
    log.info("Result: " + result + ", data: " + boardDTO.toString());

    boardDTO.setFiles(Arrays.asList(file));
      // NOTE 2021-07-04 0004 : BindingResult??

    if (result.hasErrors()) {
      return "admin/admin_board_register";
    }

    boardDTO.transViewcontent();

    // NOTE 2021-07-04 0004 : 관리자 확인 로직 추가
    if (user != null && user.getRole() == UserRoleType.ADMIN) {
//    if (user != null) {
      User admin = userService.findUserByEmail(User.builder().email(user.getEmail()).build());
      boardDTO.setUser(admin);
      boardDTO.setRegisteredAt(LocalDateTime.now());
      Board board = boardService.addBoard(boardDTO);
      boardDTO.setId(board.getId());
      attachService.addAttach(boardDTO);
    }

    redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + boardDTO.getFiles() + "!");

    return "redirect:/admin2/boards";
  }

  @GetMapping("admin2/boards/search")
  public String adminsearch(@PageableDefault Pageable pageable,@RequestParam Map<String, Object> paramMap,
                            Model model) {

    Page<Board> boards;
    if(paramMap.get("keyword") != null){
      String keyword = paramMap.get("keyword").toString();
      System.out.println(keyword);
      model.addAttribute("keyword", keyword);
      boards = boardService.searchKeyword(keyword, pageable);
      for (Board b:
            boards) {
        System.out.println(b.getId());
      }
    } else{
      boards = boardService.getBoardList(pageable);
    }

    model.addAttribute("boards", boards);



    return "admin/boards";
  }

  @GetMapping("boards/new")
  public String createForm(Model model, @LoginUser SessionUser user) throws IOException {
    model.addAttribute("boardDTO", new BoardDTO());
//    if (user != null && user.getRole() == UserRoleType.ADMIN) {
//      //      model.addAttribute("user", user);
//      return "boards/createBoardForm";
//    }
//    return "index";
    //로그인이 안되서 일단 이렇게 진행
    return "boards/createBoardForm";
  }


  @PostMapping("boards/new")
  public String create(BoardDTO boardDTO, BindingResult result, RedirectAttributes redirectAttributes, @LoginUser SessionUser user) {
    log.info("Result: " + result + ", data: " + boardDTO.toString());
    // NOTE 2021-07-04 0004 : BindingResult??
    if (result.hasErrors()) {
      return "boards/createBoardForm";
    }

    boardDTO.transViewcontent();

    // NOTE 2021-07-04 0004 : 관리자 확인 로직 추가
    if (user != null && user.getRole() == UserRoleType.ADMIN) {
//    if (user != null) {
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


  @GetMapping("boards")
  public String list(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable,
                     Model model) {
    Page<Board> boards = boardService.getBoardList(pageable);
    //    List<Board> boards = boardService.findAll();
    boardService.renewBoard();


    if(boardSearch.getBoardType() !=null){
      model.addAttribute("boardSearch", boardSearch);
    }
    model.addAttribute("boards", boards);
    System.out.println(boardSearch.toString());


    return "boards/boardList";
  }





  @GetMapping("/boards/search")
  public String search(@ModelAttribute("boardSearch") BoardSearch boardSearch, @PageableDefault Pageable pageable,
                       Model model) {
    //    System.out.println(boardSearch.getSearchType() + boardSearch.getSearchValue());
    Page<Board> boards;
    if (boardSearch.getSearchType() == SearchType.TITLE) {
      boards = boardService.searchTitle(boardSearch.getSearchValue(), boardSearch.getBoardType(), pageable);
    } else {
      boards = boardService.searchContent(boardSearch.getSearchValue(), boardSearch.getBoardType(), pageable);
    }
    model.addAttribute("boards", boards);


    if(boardSearch.getBoardType() !=null){
      model.addAttribute("boardSearch", boardSearch);
    }
    System.out.println(boardSearch.toString());

    return "boards/boardList";
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
    Board findBoard = boardService.findOne(boardId);
    boardService.upViewCnt(findBoard);
    model.addAttribute("board", findBoard);
    return "boards/board";
  }
  @GetMapping("admin2/boards/{boardId}")
  public String adminContent(@PathVariable("boardId") Long boardId, Model model, @LoginUser SessionUser user, HttpSession session) {
    //파일 리스트 보여줄 때
    //    model.addAttribute("files", storageService.loadAll().map(
    //            path -> MvcUriComponentsBuilder.fromMethodName(BoardController.class,
    //                    "serveFile", path.getFileName().toString()).build().toUri().toString())
    //            .collect(Collectors.toList()));
    model.addAttribute("files", attachService.findFiles(boardId));
    //    if (user != null) {
    //      model.addAttribute("user", user);
    //    }
    model.addAttribute("user", user);
    Board findBoard = boardService.findOne(boardId);
    try{
      System.out.println(session);
      long update_time =0;
      if(session.getAttribute("update_time"+findBoard.getId()) != null){
          update_time = (long)session.getAttribute("update_time"+findBoard.getId());
      }
      long current_time =System.currentTimeMillis();
      if(current_time - update_time > 24*60*601000){
        System.out.println("조회수 증가!!");
        boardService.upViewCnt(findBoard);
        session.setAttribute("update_time"+findBoard.getId(), current_time);
      } else System.out.println("하루가 지나야 조회수가 오름");

    } catch (Exception e){
        e.printStackTrace();
    }


    model.addAttribute("board", findBoard);


    return "admin/admin_board_view";
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
    return "boards/updateBoardForm";
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

  @GetMapping("admin2/boards/{boardId}/edit")
  public String adminUpdateBoard(@PathVariable("boardId") Long boardId, Model model, @LoginUser SessionUser user) {
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
//    try {
//      List<MultipartFile> mFiles =attachService.findMultipartFiles(boardId);
//      for(MultipartFile m : mFiles){
//        System.out.println(m.getOriginalFilename());
//      }
//      boardDTO.setFiles(mFiles);
//    } catch (Exception e){
//      System.out.println(e);
//    }

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
//
//    model.addAttribute("files", attachService.findFiles(boardId));
    model.addAttribute("boardDTO", boardDTO);
    return "admin/admin_board_update";
  }



  @PostMapping("admin2/boards/{boardId}/edit")
  public String adminUpdateBoard(@PathVariable("boardId") Long boardId, @ModelAttribute("boardDTO") BoardDTO boardDTO, @LoginUser SessionUser user, MultipartFile[] file) {
    //    System.out.println("게시글 수정!" + boardId);
    User admin = userService.findUserByEmail(User.builder().email(user.getEmail()).build());
    boardDTO.setUser(admin);
    boardDTO.setId(boardId);
    boardDTO.setRegisteredAt(LocalDateTime.now());
    boardDTO.transViewcontent();
    List<MultipartFile> mFiles = boardDTO.getFiles();
    if(mFiles !=null){
      for(MultipartFile m : file){
        mFiles.add(m);

      }
      boardDTO.setFiles(mFiles);
    } else{
      if(file != null){
        mFiles = Arrays.asList(file);
        boardDTO.setFiles(mFiles);
      }
    }



    boardService.addBoard(boardDTO);
    attachService.addAttach(boardDTO);

    return "redirect:/boards/{boardId}";
  }


  @GetMapping("/boards/{boardId}/delete")
  public String delete(@PathVariable("boardId") Long boardId, Model model) {
    Board board = boardService.findOne(boardId);
    attachService.deleteAttaches(boardId);
    boardService.delete(board);
    return "redirect:boards";
  }
  @GetMapping("admin2/boards/{boardId}/delete")
  public String adminDelete(@PathVariable("boardId") Long boardId, Model model) {
    Board board = boardService.findOne(boardId);
    attachService.deleteAttaches(boardId);
    boardService.delete(board);
    return "redirect:/admin2/boards";
  }

  @GetMapping("/api/board/search")
  public ResponseEntity<Map<String, Object>> asyncBoardSearch(HttpServletRequest request, @PageableDefault Pageable pageable) {
    System.out.println("Hi");
    Map<String, Object> map = new HashMap<>();
    String type = request.getParameter("type");
    String cls = request.getParameter("classify");
    String keyword = request.getParameter("keyword");

    List<Board>  boards;
    if (type.equals(SearchType.TITLE.name())) {
      boards = boardService.searchTitle(keyword, BoardType.valueOf(cls), pageable).get().collect(Collectors.toList());
      //boards = boardService.searchTitle(keyword, BoardType.valueOf(cls), pageable);

      map.put("data", boards);
    } else {
      boards = boardService.searchContent(keyword, BoardType.valueOf(cls), pageable).get().collect(Collectors.toList());
      //boards = boardService.searchContent(keyword, BoardType.valueOf(cls), pageable);

      map.put("data", boards);
    }

    return ResponseEntity.ok(map);
  }

  @PostMapping("/api/admin2/boards/search")
  public String asyncAdminBoardSearch(@RequestParam Map<String, Object> paramMap, @PageableDefault Pageable pageable,Model model) {


    String keyword = paramMap.get("keyword").toString();
    System.out.println(keyword);
    //List<Board> boards = boardService.searchContent(keyword, pageable).get().collect(Collectors.toList());;
    Page<Board> boards = boardService.searchKeyword(keyword, pageable);
    for(Board b : boards){
      System.out.println(b.getId());
    }
    model.addAttribute("keyword", keyword);
    model.addAttribute("boards", boards);


    return "/fragments/boardList";
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

  @GetMapping("api/{file}/delete")
  public ResponseEntity asyncDeleteFile( @PathVariable("file") String filename, Model model) {
    System.out.println("파일 삭제 성공!");
    attachService.delete(attachService.findByname(filename).get(0));
    return new ResponseEntity(HttpStatus.OK);
  }

//  @GetMapping("/api/admin2/boards/search")
//  public String asyncAdminBoardSearch2(@RequestParam Map<String, Object> paramMap, @PageableDefault Pageable pageable,Model model){
//    String keyword = paramMap.get("keyword").toString();
//    System.out.println(paramMap);
//    System.out.println(keyword);
//    //List<Board> boards = boardService.searchContent(keyword, pageable).get().collect(Collectors.toList());;
//    Page<Board> boards = boardService.searchKeyword(keyword, pageable);
//    for(Board b : boards){
//      System.out.println(b.getId());
//    }
//    model.addAttribute("keyword", keyword);
//    model.addAttribute("boards", boards);
//
//
//    return "/fragments/boardList";
//  }


}
