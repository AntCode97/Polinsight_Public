package com.dns.polinsight.controller;

import com.dns.polinsight.config.resolver.CurrentUser;
import com.dns.polinsight.domain.Comment;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.CommentDto;
import com.dns.polinsight.service.CommentService;
import com.dns.polinsight.service.PostService;
import com.dns.polinsight.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.dns.polinsight.utils.ApiUtils.success;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  private final PostService postService;

  @GetMapping("/comment/{commentId}")
  public ApiUtils.ApiResult<Optional<Comment>> findOneCommentByCommentId(@PathVariable("commentId") Long commentId) {
    return success(commentService.findOneCommentBySeq(commentId));
  }

  @GetMapping("/comments")
  public ApiUtils.ApiResult<Page<Comment>> findAll(@PageableDefault Pageable pageable) {
    return success(commentService.findAllComment(pageable));
  }

  @Transactional
  @PostMapping("/comment/{postId}")
  public ApiUtils.ApiResult<Boolean> addComment(@RequestBody CommentDto dto,
                                                @PathVariable("postId") Long postId,
                                                @CurrentUser User user) throws Exception {
    log.warn("댓글 추가 ---- {}", postId);
    log.warn(dto.toString());
    try {
      Post post = postService.findOne(postId);
      dto.setAuthor(user);
      dto.setPost(post);
      dto.setNumber(post.getComments().size() + 1);
      Comment comment = commentService.saveAndUpdate(Comment.of(dto));
      post.getComments().add(comment);
      postService.updatePost(post);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @Transactional
  @PatchMapping("/comment")
  public ApiUtils.ApiResult<Boolean> udateComment(@RequestBody CommentDto dto) throws Exception {
    try {
      Comment savedComment = commentService.findOneCommentBySeq(dto.getSeq()).orElseThrow();
      savedComment.update(dto);
      commentService.saveAndUpdate(savedComment);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @Transactional
  @DeleteMapping("/comment/{commentId}")
  public ApiUtils.ApiResult<Boolean> deleteComment(@PathVariable("commentId") Long commentId) throws Exception {
    try {
      commentService.deleteComment(commentId);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
