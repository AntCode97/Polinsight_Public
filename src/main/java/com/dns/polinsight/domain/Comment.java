package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Comparable<Comment> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long seq;

  private Integer number;

  @JsonBackReference
  @OneToOne(targetEntity = User.class)
  private User author;

  private String content;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "pno")
  private Post post;

  private LocalDateTime registeredAt;

  private LocalDateTime lastModifiedAt;

  /*
   * 수정, 삭제 여부등을 저장할 필드
   * */
  private Boolean isDeleted;

  public static Comment of(CommentDto dto) {
    return Comment.builder()
                  .seq(dto.getSeq())
                  .author(dto.getAuthor())
                  .content(dto.getContent())
                  .post(dto.getPost())
                  .isDeleted(dto.getIsDeleted())
                  .lastModifiedAt(dto.getLastModifiedAt())
                  .number(dto.getNumber())
                  .build();
  }

  public void update(CommentDto dto) {
    this.content = dto.getContent();
    this.lastModifiedAt = LocalDateTime.now();
  }

  @PrePersist
  public void registryTime() {
    this.registeredAt = LocalDateTime.now();
  }

  @Override
  public int compareTo(@NotNull Comment o) {
    return Long.compare(this.number, o.number);
  }

}
