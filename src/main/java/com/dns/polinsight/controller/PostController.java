package com.dns.polinsight.controller;

import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.AttachNotFoundException;
import com.dns.polinsight.exception.PointCalculateException;
import com.dns.polinsight.repository.PostSearch;
import com.dns.polinsight.service.AttachService;
import com.dns.polinsight.service.PostService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.storage.StorageFileNotFoundException;
import com.dns.polinsight.storage.StorageService;
import com.dns.polinsight.types.PostType;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
public class PostController {

  private final UserService userService;

  private final PostService postService;

  private final StorageService storageService;

  private final AttachService attachService;


  @GetMapping("admin/posts")
  public String adminPostList(@ModelAttribute("postSearch") PostSearch postSearch, @PageableDefault Pageable pageable, Model model, @RequestParam Map<String, Object> paramMap, HttpSession session) {


    Page<com.dns.polinsight.mapper.PostMapping> posts;
    if (paramMap.get("keyword") != null) {
      String keyword = paramMap.get("keyword").toString();
      if (keyword.equals("")) {
        model.addAttribute("keyword", keyword);
        posts = postService.findBySearchKeyword(keyword, PostType.NOTICE, pageable);

        long postCount = posts.getTotalElements();
        model.addAttribute("postCount", postCount);
        session.setAttribute("postCount", postCount);
        model.addAttribute("posts", posts);
      }
    } else {
      posts = postService.findPostsByType(PostType.NOTICE, pageable);
      long postCount = posts.getTotalElements();
      model.addAttribute("postCount", postCount);
      session.setAttribute("postCount", postCount);
      model.addAttribute("posts", posts);
    }


    return "admin/admin_post_list";
  }


  @GetMapping("admin/posts/new")
  public String adminCreateForm(Model model, @AuthenticationPrincipal User user) throws IOException {
    model.addAttribute("postDTO", new PostDTO());
    model.addAttribute("user", user);
    //    if (user != null && user.getRole() == UserRoleType.ADMIN) {
    //      //      model.addAttribute("user", user);
    //      return "posts/createPostForm";
    //    }
    //    return "index";
    //로그인이 안되서 일단 이렇게 진행
    return "admin/admin_post_register";
  }


  @PostMapping("admin/posts/new")
  public String adminCreate(PostDTO postDTO, BindingResult result, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user, MultipartFile[] file) {
    log.info("Result: " + result + ", data: " + postDTO.toString());

    postDTO.setFiles(Arrays.asList(file));
    if (result.hasErrors()) {
      return "admin/admin_post_register";
    }
    postDTO.transViewcontent();
    if (user != null && user.getRole() == UserRoleType.ADMIN) {
      //    if (user != null) {
      User admin = userService.findUserByEmail(user.getEmail());
      postDTO.setUser(admin);
      postDTO.setRegisteredAt(LocalDateTime.now());
      Post post = postService.addPost(postDTO);
      postDTO.setId(post.getId());
      attachService.addAttach(postDTO);
    }

    redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + postDTO.getFiles() + "!");

    return "redirect:/admin/posts";
  }

  @GetMapping("admin/posts/search")
  public String adminsearch(@PageableDefault Pageable pageable, @RequestParam Map<String, Object> paramMap,
                            Model model, HttpSession session) {

    Page<Post> posts;
    if (paramMap.get("keyword") != null) {
      String keyword = paramMap.get("keyword").toString();

      model.addAttribute("keyword", keyword);
      posts = postService.searchKeyword(keyword, pageable);
    } else {
      posts = postService.getPostList(pageable);
    }
    long postCount = posts.getTotalElements();

    model.addAttribute("postCount", postCount);
    session.setAttribute("postCount", postCount);
    model.addAttribute("posts", posts);


    return "admin/admin_post_list";
  }

  @GetMapping("posts/new")
  public String createForm(Model model, @AuthenticationPrincipal User user) throws IOException {
    model.addAttribute("postDTO", new PostDTO());
    model.addAttribute("user", user);
    //    if (user != null && user.getRole() == UserRoleType.ADMIN) {
    //      //      model.addAttribute("user", user);
    //      return "posts/createPostForm";
    //    }
    //    return "index";
    //로그인이 안되서 일단 이렇게 진행
    return "posts/createPostForm";
  }


  @PostMapping("posts/new")
  public String create(PostDTO postDTO, BindingResult result, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user, MultipartFile[] file) {
    postDTO.setFiles(Arrays.asList(file));
    log.info("Result: " + result + ", data: " + postDTO.toString());
    if (result.hasErrors()) {
      return "/posts/createPostForm";
    }
    postDTO.transViewcontent();
    if (user != null && user.getRole() == UserRoleType.ADMIN) {
      //    if (user != null) {
      User admin = userService.findUserByEmail(user.getEmail());
      postDTO.setUser(admin);
      postDTO.setRegisteredAt(LocalDateTime.now());
      Post post = postService.addPost(postDTO);
      postDTO.setId(post.getId());
      attachService.addAttach(postDTO);
    }

    redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + postDTO.getFiles() + "!");

    return "redirect:/posts";
  }


  @GetMapping("posts")
  public String list(@ModelAttribute("postSearch") PostSearch postSearch, @PageableDefault Pageable pageable,
                     Model model) {
    //Page<Post> posts = postService.getPostList(pageable);
    Page<com.dns.polinsight.mapper.PostMapping> posts = postService.findPostsByType(PostType.NOTICE, pageable);

    //posts.get().map(Post::toString);

    if (postSearch.getPostType() != null) {
      model.addAttribute("postSearch", postSearch);
    }
    model.addAttribute("posts", posts);


    return "posts/postList";
  }


  @GetMapping("posts/search")
  public String search(@ModelAttribute("postSearch") PostSearch postSearch, @PageableDefault Pageable pageable,
                       Model model) {
    //    System.out.println(postSearch.getSearchType() + postSearch.getSearchValue());
    Page<com.dns.polinsight.mapper.PostMapping> posts;
    if (postSearch.getSearchType() == SearchType.TITLE) {
      posts = postService.findPostsByTitle(postSearch.getSearchValue(), postSearch.getPostType(), pageable);
    } else {
      posts = postService.findPostsBySearchcontent(postSearch.getSearchValue(), postSearch.getPostType(), pageable);
    }
    model.addAttribute("posts", posts);


    if (postSearch.getPostType() != null) {
      model.addAttribute("postSearch", postSearch);
    }


    return "posts/postList";
  }


  @GetMapping("posts/{postId}")
  public String content(@PathVariable("postId") Long postId, Model model, @AuthenticationPrincipal User user, HttpSession session) {
    //파일 리스트 보여줄 때
    //    model.addAttribute("files", storageService.loadAll().map(
    //            path -> MvcUriComponentsBuilder.fromMethodName(PostController.class,
    //                    "serveFile", path.getFileName().toString()).build().toUri().toString())
    //            .collect(Collectors.toList()));

    model.addAttribute("user", user);
    model.addAttribute("files", attachService.findFiles(postId));
    //    if (user != null) {
    //      model.addAttribute("user", user);
    //    }

    try {
      long update_time = 0;
      if (session.getAttribute("update_time" + postId) != null) {
        update_time = (long) session.getAttribute("update_time" + postId);
      }
      long current_time = System.currentTimeMillis();
      if (current_time - update_time > 24 * 60 * 601000) {
        log.info("Post View COUNT UP");
        postService.upViewCnt(postId);
        session.setAttribute("update_time" + postId, current_time);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    Post findPost = postService.findOne(postId);

    model.addAttribute("post", new PostDTO(findPost));
    List<Post> allPosts = postService.findAll();
    for (int i = 0; i < allPosts.size(); i++) {
      if (allPosts.get(i).getId() == postId) {
        if (i != 0) {
          model.addAttribute("prevPost", allPosts.get(i - 1));
        }
        if (i != allPosts.size() - 1) {
          model.addAttribute("nextPost", allPosts.get(i + 1));
        }
        break;
      }
    }

    return "posts/post";
  }

  @GetMapping("admin/posts/{postId}")
  public String adminContent(@PathVariable("postId") Long postId, Model model, @AuthenticationPrincipal User user, HttpSession session) {
    //파일 리스트 보여줄 때
    //    model.addAttribute("files", storageService.loadAll().map(
    //            path -> MvcUriComponentsBuilder.fromMethodName(PostController.class,
    //                    "serveFile", path.getFileName().toString()).build().toUri().toString())
    //            .collect(Collectors.toList()));
    model.addAttribute("files", attachService.findFiles(postId));
    //    if (user != null) {
    //      model.addAttribute("user", user);
    //    }
    model.addAttribute("user", user);


    try {
      long update_time = 0;
      if (session.getAttribute("update_time" + postId) != null) {
        update_time = (long) session.getAttribute("update_time" + postId);
      }
      long current_time = System.currentTimeMillis();
      if (current_time - update_time > 24 * 60 * 601000) {
        postService.upViewCnt(postId);
        log.info("Post View COUNT UP");
        session.setAttribute("update_time" + postId, current_time);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    Post findPost = postService.findOne(postId);
    model.addAttribute("post", new PostDTO(findPost));
    return "admin/admin_post_view";
  }

  @GetMapping("posts/{postId}/edit")
  public String updatePost(@PathVariable("postId") Long postId, Model model, @AuthenticationPrincipal User user) {

    if (user != null && (user.getRole() == UserRoleType.USER || user.getRole() == UserRoleType.PANEL || user.getRole() == UserRoleType.BEST
        || user.getRole() == UserRoleType.ADMIN)) {
      Post post = postService.findOne(postId);
      PostDTO postDTO = new PostDTO();
      postDTO.setId(post.getId());
      postDTO.setContent(post.getSearchcontent());
      postDTO.setViewcontent(post.getViewcontent());
      postDTO.setAttaches(post.getAttaches());
      postDTO.setUser(post.getUser());
      postDTO.setTitle(post.getTitle());
      LocalDateTime registeredAt = LocalDateTime.now();
      postDTO.setRegisteredAt(registeredAt);

      model.addAttribute("files", attachService.findFiles(postId));

      model.addAttribute("postDTO", postDTO);
      return "/posts/updatePostForm";
    } else {
      return "redirect:/posts";
    }


  }

  @PostMapping("posts/{postId}/edit")
  public String updatePost(@PathVariable("postId") Long postId, @ModelAttribute("postDTO") PostDTO postDTO, @AuthenticationPrincipal User user, MultipartFile[] file) {
    if (user != null && (user.getRole() == UserRoleType.USER || user.getRole() == UserRoleType.PANEL || user.getRole() == UserRoleType.BEST
        || user.getRole() == UserRoleType.ADMIN)) {
      User admin = userService.findUserByEmail(user.getEmail());
      postDTO.setUser(admin);
      postDTO.setId(postId);
      postDTO.setRegisteredAt(LocalDateTime.now());
      postDTO.transViewcontent();
      postDTO.setViewcnt(postService.findOne(postId).getViewcnt());
      List<MultipartFile> mFiles = postDTO.getFiles();
      if (mFiles != null) {
        for (MultipartFile m : file) {
          mFiles.add(m);

        }
        postDTO.setFiles(mFiles);
      } else {
        if (file != null) {
          mFiles = Arrays.asList(file);
          postDTO.setFiles(mFiles);
        }
      }

      postService.addPost(postDTO);
      attachService.addAttach(postDTO);
    }
    return "redirect:/posts/{postId}";
  }

  @GetMapping("admin/posts/{postId}/edit")
  public String adminUpdatePost(@PathVariable("postId") Long postId, Model model, @AuthenticationPrincipal User user) {

    Post post = postService.findOne(postId);
    PostDTO postDTO = new PostDTO();
    postDTO.setId(post.getId());
    postDTO.setContent(post.getSearchcontent());
    postDTO.setViewcontent(post.getViewcontent());
    postDTO.setAttaches(post.getAttaches());
    postDTO.setUser(post.getUser());
    postDTO.setTitle(post.getTitle());
    LocalDateTime registeredAt = LocalDateTime.now();
    postDTO.setRegisteredAt(registeredAt);

    model.addAttribute("postDTO", postDTO);
    return "admin/admin_post_update";
  }


  @PostMapping("admin/posts/{postId}/edit")
  public String adminUpdatePost(@PathVariable("postId") Long postId, @ModelAttribute("postDTO") PostDTO postDTO, @AuthenticationPrincipal User user, MultipartFile[] file) {
    //    System.out.println("게시글 수정!" + postId);
    User admin = userService.findUserByEmail(user.getEmail());
    postDTO.setUser(admin);
    postDTO.setId(postId);
    postDTO.setRegisteredAt(LocalDateTime.now());
    postDTO.setViewcnt(postService.findOne(postId).getViewcnt());
    postDTO.transViewcontent();
    //TODO: 조회수 항목도 가져오기
    List<MultipartFile> mFiles = postDTO.getFiles();
    if (mFiles != null) {
      for (MultipartFile m : file) {
        mFiles.add(m);

      }
      postDTO.setFiles(mFiles);
    } else {
      if (file != null) {
        mFiles = Arrays.asList(file);
        postDTO.setFiles(mFiles);
      }
    }


    postService.addPost(postDTO);
    attachService.addAttach(postDTO);

    return "redirect:/admin/posts/{postId}";
  }


  @GetMapping("/posts/{postId}/delete")
  public String delete(@PathVariable("postId") Long postId, Model model) {
    Post post = postService.findOne(postId);
    attachService.deleteAttaches(postId);
    postService.delete(post);
    return "redirect:/posts";
  }

  //Post로 바꿔도 될듯함
  @GetMapping("admin/posts/{postId}/delete")
  public String adminDelete(@PathVariable("postId") Long postId, Model model, @PageableDefault Pageable pageable) {
    Post post = postService.findOne(postId);
    attachService.deleteAttaches(postId);
    postService.delete(post);
    //Page<Post> posts = postService.getPostList(pageable);
    //model.addAttribute("posts", posts);
    return "redirect:/admin/posts";
    //return "fragments/postList";
  }

  @GetMapping("/api/post/search")
  public ResponseEntity<Map<String, Object>> asyncPostSearch(HttpServletRequest request, @PageableDefault Pageable pageable) {
    Map<String, Object> map = new HashMap<>();
    String type = request.getParameter("type");
    String cls = request.getParameter("classify");
    String keyword = request.getParameter("keyword");

    List<Post> posts;
    if (type.equals(SearchType.TITLE.name())) {
      posts = postService.searchTitle(keyword, PostType.valueOf(cls), pageable).get().collect(Collectors.toList());
      //posts = postService.searchTitle(keyword, PostType.valueOf(cls), pageable);

      map.put("data", posts);
    } else {
      posts = postService.searchContent(keyword, PostType.valueOf(cls), pageable).get().collect(Collectors.toList());
      //posts = postService.searchContent(keyword, PostType.valueOf(cls), pageable);

      map.put("data", posts);
    }

    return ResponseEntity.ok(map);
  }

  @PostMapping("/api/admin/posts/search")
  public String asyncAdminPostSearch(@RequestParam Map<String, Object> paramMap, @PageableDefault Pageable pageable, Model model, HttpSession session) {


    String keyword = paramMap.get("keyword").toString();
    //List<Post> posts = postService.searchContent(keyword, pageable).get().collect(Collectors.toList());;


    Page<com.dns.polinsight.mapper.PostMapping> posts = postService.findBySearchKeyword(keyword, PostType.NOTICE, pageable);

    model.addAttribute("keyword", keyword);
    model.addAttribute("posts", posts);
    session.setAttribute("postCount", posts.getTotalElements());
    model.addAttribute("postCount", posts.getTotalElements());
    return "fragments/postList :: #boardTable";
  }

  @PostMapping("/api/admin/posts/search/count")
  public String asyncPostCount(Model model, HttpSession session, @RequestParam("keyword") String keyword) {

    System.out.println(keyword);
    model.addAttribute("keyword", keyword);
    //    String keyword = paramMap.get("keyword").toString();
    //    //List<Post> posts = postService.searchContent(keyword, pageable).get().collect(Collectors.toList());;
    //    Page<Post> posts = postService.searchKeyword(keyword, pageable);
    //    model.addAttribute("keyword", keyword);
    //    model.addAttribute("posts", posts);
    //    session.setAttribute("postCount", posts.getTotalElements());
    model.addAttribute("postCount", session.getAttribute("postCount"));

    return "fragments/postList :: #postCount";
  }

  //파일 클릭했을 때, 다운로드할 수 있게 함
  @GetMapping("/posts/upload-dir/{filename}")
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


  @GetMapping("/posts/{postId}/{file}/delete")
  public String deleteFile(@PathVariable("postId") Long postId, @PathVariable("file") String filename, Model model) {
    attachService.delete(attachService.findByname(filename).get(0));
    return "redirect:/posts/" + postId + "/edit";
  }

  @GetMapping("api/{file}/delete")
  public ResponseEntity asyncDeleteFile(@PathVariable("file") String filename, Model model) {
    log.info("File Delete Success!!");
    attachService.delete(attachService.findByname(filename).get(0));
    return new ResponseEntity(HttpStatus.OK);
  }


}
