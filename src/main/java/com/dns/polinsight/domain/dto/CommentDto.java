package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

  @Setter
  private Integer number;

  private String content;

  @JsonBackReference
  @Setter
  private Post post;

  private LocalDateTime lastModifiedAt;

  @JsonBackReference
  @Setter
  private User author;

  private Boolean isDeleted;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
