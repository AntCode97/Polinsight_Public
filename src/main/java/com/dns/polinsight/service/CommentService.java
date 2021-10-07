package com.dns.polinsight.service;

import com.dns.polinsight.domain.Comment;
import com.dns.polinsight.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {

  Comment saveAndUpdate(Comment comment);

  Page<Comment> findAllComment(Pageable pageable);

  List<Comment> findAllComment();

  Optional<Comment> findOneCommentBySeq(Long commentId);

  List<Comment> findAllCommentByPostId(Post post);

  void deleteComment(Long commentId);
}
