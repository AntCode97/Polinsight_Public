package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Comment;
import com.dns.polinsight.domain.Post;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

  private Long seq;

  private String content;

  private Long postId;

  @Setter
  private Post post;

  private LocalDateTime lastModifiedAt;

  @Setter
  private String writer;

  private Boolean isDeleted;

  public CommentDto(Comment comment) {
    this.seq = comment.getSeq();
    this.content = comment.getContent();
    //    this.post = comment.getPost();
    this.postId = comment.getPost().getId();
    this.writer = comment.getWriter();
    this.lastModifiedAt = comment.getLastModifiedAt();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
