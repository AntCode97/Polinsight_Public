package com.dns.polinsight.service;

import com.dns.polinsight.domain.Comment;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  @Override
  public Comment saveAndUpdate(Comment comment) {
    return commentRepository.saveAndFlush(comment);
  }

  @Override
  public Page<Comment> findAllComment(Pageable pageable) {
    return commentRepository.findAll(pageable);
  }

  @Override
  public List<Comment> findAllComment() {
    return commentRepository.findAll();
  }

  @Override
  public Optional<Comment> findOneCommentBySeq(Long commentId) {
    return commentRepository.findCommentBySeq(commentId);
  }

  @Override
  public List<Comment> findAllCommentByPostId(Post post) {
    return commentRepository.findAllByPost(post);
  }

  @Override
  public void deleteComment(Long commentId) {
    commentRepository.deleteById(commentId);
  }

}
