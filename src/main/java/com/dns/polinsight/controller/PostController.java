package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.ImageResizeException;
import com.dns.polinsight.repository.PostSearch;
import com.dns.polinsight.service.AttachService;
import com.dns.polinsight.service.PostService;
import com.dns.polinsight.service.UserService;
import com.dns.polinsight.storage.StorageService;
import com.dns.polinsight.types.PostType;
import com.dns.polinsight.types.SearchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    Page<com.dns.polinsight.projection.PostMapping> posts;
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

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("admin/posts/new")
  public String adminCreate(PostDTO postDTO, BindingResult result, RedirectAttributes redirectAttributes, @CurrentUser User user, MultipartFile[] file) throws ImageResizeException, IOException {
    if (result.hasErrors()) {
      return "admin/admin_post_register";
    }
    postDTO.setFiles(Arrays.asList(file));
    postDTO.transViewcontent();
    User admin = userService.findUserByEmail(user.getEmail());
    postDTO.setUser(admin);
    postDTO.setRegisteredAt(LocalDateTime.now());
    Post post = postService.addPost(postDTO);
    postDTO.setId(post.getId());
    //썸네일 추가
    MultipartFile originalThumbnail = postDTO.getThumbnailImg();
    if (originalThumbnail != null && !originalThumbnail.isEmpty()) {
      UUID uuid = UUID.randomUUID();
      String thumbnailPath = storageService.saveThumbnail(uuid.toString(), originalThumbnail);
      postDTO.setThumbnail(thumbnailPath);
      storageService.store(uuid.toString(), originalThumbnail);
      log.info("Success add thumbnail");
    } else {
      log.error("There is no Thumbnail");
    }
    //파일 첨부
    List<MultipartFile> files = postDTO.getFiles();
    if (files != null) {
      if (!files.isEmpty()) {
        saveAttaches(postDTO, files);
      }
    } else {
      log.info("File List is null");
    }
    postService.addPost(postDTO);
    redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + postDTO.getFiles() + "!");

    return "redirect:/admin/posts";
  }

  private void saveAttaches(PostDTO postDTO, List<MultipartFile> files) throws IOException {
    List<Attach> attaches = new ArrayList<>();
    for (MultipartFile mf : files) {
      if (!mf.isEmpty()) {
        UUID uuid = UUID.randomUUID();
        attaches.add(attachService.addAttach(uuid, mf, postDTO));
        storageService.store(uuid.toString(), mf);

      }
    }
    postDTO.setAttaches(attaches);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("admin/posts/search")
  public String adminSearch(@PageableDefault Pageable pageable,
                            @RequestParam Map<String, Object> paramMap,
                            Model model, HttpSession session) {
    Page<Post> posts;
    if (paramMap.get("keyword") != null) {
      String keyword = paramMap.get("keyword").toString();
      model.addAttribute("keyword", keyword);
      posts = postService.searchKeyword(keyword, pageable);
    } else {
      posts = postService.getPostList(pageable);
    }
    model.addAttribute("postCount", posts.getTotalElements());
    session.setAttribute("postCount", posts.getTotalElements());
    model.addAttribute("posts", posts);


    return "admin/admin_post_list";
  }

  @GetMapping("posts")
  public String list(@ModelAttribute("postSearch") PostSearch postSearch,
                     @PageableDefault Pageable pageable,
                     Model model) {
    if (postSearch.getPostType() != null) {
      model.addAttribute("postSearch", postSearch);
    }
    model.addAttribute("posts", postService.findPostsByType(PostType.NOTICE, pageable));

    return "posts/postList";
  }


  @GetMapping("posts/search")
  public String search(@ModelAttribute("postSearch") PostSearch postSearch,
                       @PageableDefault Pageable pageable,
                       Model model) {
    Page<com.dns.polinsight.projection.PostMapping> posts;
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
  public String content(@PathVariable("postId") Long postId, Model model, HttpSession session) {

    model.addAttribute("files", attachService.findFiles(postId));

    try {
      long update_time = 0;
      if (session.getAttribute("update_time" + postId) != null) {
        update_time = (long) session.getAttribute("update_time" + postId);
      }
      long current_time = System.currentTimeMillis();
      if (current_time - update_time > 24 * 60 * 601000) {
        postService.upViewCnt(postId);
        log.info("Post No.{} view count up", postId);
        session.setAttribute("update_time" + postId, current_time);
      }

    } catch (Exception e) {
      // NOTE 2021-10-13 : try-catch 사용 이유??? / 어떤 에러가 발생하는지??
      e.printStackTrace();
    }
    Post findPost = postService.findOne(postId);
    model.addAttribute("post", new PostDTO(findPost));

    // NOTE 2021-10-13 : 아래 코드는 무슨 작동을 위해??
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

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("admin/posts/{postId}")
  public String adminContent(@PathVariable("postId") Long postId, Model model, HttpSession session) {
    model.addAttribute("files", attachService.findFiles(postId));
    try {
      long update_time = 0;
      if (session.getAttribute("update_time" + postId) != null) {
        update_time = (long) session.getAttribute("update_time" + postId);
      }
      long current_time = System.currentTimeMillis();
      if (current_time - update_time > 24 * 60 * 601000) {
        postService.upViewCnt(postId);
        log.info("Post No.{} view count up", postId);
        session.setAttribute("update_time" + postId, current_time);
      }

      Post findPost = postService.findOne(postId);
      model.addAttribute("post", new PostDTO(findPost));
    } catch (Exception e) {
      // NOTE 2021-10-13 : try-catch 사용 이유??? / 어떤 에러가 발생하는지??
      e.printStackTrace();
    }
    return "admin/admin_post_view";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("admin/posts/{postId}/edit")
  public String adminUpdatePost(@PathVariable("postId") Long postId, Model model) {
    Post post = postService.findOne(postId);
    PostDTO postDTO = PostDTO.of(post);
    postDTO.setRegisteredAt(LocalDateTime.now());
    model.addAttribute("postDTO", postDTO);
    return "admin/admin_post_update";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("admin/posts/{postId}/edit")
  public String adminUpdatePost(@PathVariable("postId") Long postId,
                                @ModelAttribute("postDTO") PostDTO postDTO,
                                @CurrentUser User user,
                                MultipartFile[] files) throws IOException, ImageResizeException {
    postDTO.setUser(userService.findUserByEmail(user.getEmail()));
    postDTO.setId(postId);
    postDTO.setRegisteredAt(LocalDateTime.now());
    postDTO.setViewcnt(postService.findOne(postId).getViewcnt());
    postDTO.transViewcontent();
    if (postDTO.getPostType() == null)
      postDTO.setPostType(PostType.NOTICE);
    List<MultipartFile> mFiles = postDTO.getFiles();
    if (mFiles != null) {
      mFiles.addAll(Arrays.asList(files));
    } else {
      if (files != null) {
        mFiles = Arrays.asList(files);
      }
    }
    postDTO.setFiles(mFiles);

    Post editPost = postService.findOne(postId);

    //썸네일 추가
    MultipartFile thumbnailImg = postDTO.getThumbnailImg();
    //새로운 썸네일 이미지가 있는데
    if (thumbnailImg != null && !thumbnailImg.isEmpty()) {
      //기존의 게시글도 썸네일이 있을 때
      if (editPost.getThumbnail() != null) {
        storageService.delete(editPost.getThumbnail());
      }
      UUID uuid = UUID.randomUUID();
      postDTO.setThumbnail(storageService.saveThumbnail(uuid.toString(), thumbnailImg));
      storageService.store(uuid.toString(), thumbnailImg);
      log.info("Success add thumbnail");
    } else {
      //원래 썸네일 유지
      postDTO.setThumbnail(editPost.getThumbnail());
    }

    //파일 첨부
    List<MultipartFile> fileList = postDTO.getFiles();
    if (fileList != null) {
      saveAttaches(postDTO, fileList);
    }
    postService.addPost(postDTO);
    return "redirect:/admin/posts/{postId}";
  }


  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseBody
  @DeleteMapping("/admin/posts/{postId}/delete")
  public String adminDelete(@PathVariable("postId") Long postId) throws FileNotFoundException {

    Post post = postService.findOne(postId);

    deleteAttaches(postId);
    if (post.getThumbnail() != null) {
      storageService.delete(post.getThumbnail());
    }
    postService.delete(post);
    return "redirect:/admin/posts";
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
    } else {
      posts = postService.searchContent(keyword, PostType.valueOf(cls), pageable).get().collect(Collectors.toList());
    }
    map.put("data", posts);

    return ResponseEntity.ok(map);
  }

  @PostMapping("/api/admin/posts/search")
  public String asyncAdminPostSearch(@RequestParam Map<String, Object> paramMap, @PageableDefault Pageable pageable, Model model, HttpSession session) {


    String keyword = paramMap.get("keyword").toString();
    String type = paramMap.get("type").toString();
    Page<com.dns.polinsight.projection.PostMapping> posts = postService.findBySearchKeyword(keyword, PostType.valueOf(type), pageable);
    model.addAttribute("keyword", keyword);
    model.addAttribute("posts", posts);
    session.setAttribute("postCount", posts.getTotalElements());
    model.addAttribute("postCount", posts.getTotalElements());
    return "fragments/postList :: #boardTable";
  }

  @PostMapping("/api/admin/posts/search/count")
  public String asyncPostCount(Model model, HttpSession session, @RequestParam("keyword") String keyword) {

    model.addAttribute("keyword", keyword);
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

  @Transactional
  @GetMapping("api/{file}/delete")
  public ResponseEntity<Boolean> asyncDeleteFile(@PathVariable("file") String filename) throws FileNotFoundException {
    deleteAttach(attachService.findByname(filename).get(0));
    log.info("{} Delete Success!!", filename);
    return ResponseEntity.ok(true);
  }


  @GetMapping("posts/pols/{pno}")
  public ModelAndView getOnePolsinsight(ModelAndView mv, @PathVariable("pno") Long pno) {
    mv.addObject("data", PostDTO.of(postService.findOne(pno)));
    mv.setViewName("posts/insight");
    return mv;
  }

  public void deleteAttach(Attach attach) throws FileNotFoundException {
    // 실제 경로의 파일을 지운다.
    storageService.delete(attach.getFilePath());
    // DB에 저장된 경로를 지운다.
    attachService.delete(attach);

  }

  public void deleteAttaches(Long postId) {
    attachService.findByPostId(postId).forEach(attach -> {
      try {
        storageService.delete(attach.getFilePath());
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      attachService.delete(attach);
    });

  }


  //현재 일반 유저는 글을 쓸 수 없기 떄문에 주석처리
  @GetMapping("posts/new")
  @PreAuthorize("isAuthenticated()")
  public String createForm(Model model, @CurrentUser User user) throws IOException {
    model.addAttribute("postDTO", new PostDTO());
    model.addAttribute("user", user);
    return "posts/createPostForm";
  }
  //
  //    @PostMapping("posts/new")
  //    @PreAuthorize("isAuthenticated()")
  //    public String create(PostDTO postDTO, BindingResult result, RedirectAttributes redirectAttributes, @CurrentUser User user, MultipartFile[] file) {
  //      postDTO.setFiles(Arrays.asList(file));
  //      log.info("Result: " + result + ", data: " + postDTO);
  //      if (result.hasErrors()) {
  //        return "/posts/createPostForm";
  //      }
  //      postDTO.transViewcontent();
  //      User admin = userService.findUserByEmail(user.getEmail());
  //      postDTO.setUser(admin);
  //      postDTO.setRegisteredAt(LocalDateTime.now());
  //      Post post = postService.addPost(postDTO);
  //      postDTO.setId(post.getId());
  //      attachService.addAttach(postDTO);
  //      redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + postDTO.getFiles() + "!");
  //      return "redirect:/posts";
  //    }

  //  @PreAuthorize("isAuthenticated()")
  //  @GetMapping("posts/{postId}/edit")
  //  public String updatePost(@PathVariable("postId") Long postId, Model model) {
  //
  //
  //    Post post = postService.findOne(postId);
  //    PostDTO postDTO = new PostDTO();
  //    postDTO.setId(post.getId());
  //    postDTO.setContent(post.getSearchcontent());
  //    postDTO.setViewcontent(post.getViewcontent());
  //    postDTO.setAttaches(post.getAttaches());
  //    postDTO.setUser(post.getUser());
  //    postDTO.setTitle(post.getTitle());
  //    LocalDateTime registeredAt = LocalDateTime.now();
  //    postDTO.setRegisteredAt(registeredAt);
  //
  //    model.addAttribute("files", attachService.findFiles(postId));
  //
  //    model.addAttribute("postDTO", postDTO);
  //    return "/posts/updatePostForm";
  //
  //  }
  //
  //  @PreAuthorize("isAuthenticated()")
  //  @PostMapping("posts/{postId}/edit")
  //  public String updatePost(@PathVariable("postId") Long postId, @ModelAttribute("postDTO") PostDTO postDTO, @CurrentUser User user, MultipartFile[] file) {
  //
  //    User admin = userService.findUserByEmail(user.getEmail());
  //    postDTO.setUser(admin);
  //    postDTO.setId(postId);
  //    postDTO.setRegisteredAt(LocalDateTime.now());
  //    postDTO.transViewcontent();
  //    postDTO.setViewcnt(postService.findOne(postId).getViewcnt());
  //    List<MultipartFile> mFiles = postDTO.getFiles();
  //    if (mFiles != null) {
  //      for (MultipartFile m : file) {
  //        mFiles.add(m);
  //
  //      }
  //      postDTO.setFiles(mFiles);
  //    } else {
  //      if (file != null) {
  //        mFiles = Arrays.asList(file);
  //        postDTO.setFiles(mFiles);
  //      }
  //    }
  //
  //    postService.addPost(postDTO);
  //    attachService.addAttach(postDTO);
  //
  //    return "redirect:/posts/{postId}";
  //  }

  //@PreAuthorize("isAuthenticated()")
  //@GetMapping("/posts/{postId}/delete")
  //public String delete(@PathVariable("postId") Long postId, Model model) {
  //  Post post = postService.findOne(postId);
  //  attachService.deleteAttaches(postId);
  //  if (post.getThumbnail() != null) {
  //    attachService.deleteThumbnail(post.getThumbnail());
  //  }
  //  postService.delete(post);
  //  return "redirect:/posts";
  //}

}
