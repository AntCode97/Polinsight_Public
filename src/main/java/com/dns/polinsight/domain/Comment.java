package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Setter
  private String writer;

  @Column(nullable = false)
  private String content;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "pno")
  private Post post;

  private LocalDateTime registeredAt;

  private LocalDateTime lastModifiedAt;

  private Boolean isDeleted;

  public static Comment of(CommentDto dto) {
    return Comment.builder()
                  .seq(dto.getSeq())
                  .content(dto.getContent())
                  .post(dto.getPost())
                  .isDeleted(dto.getIsDeleted())
                  .lastModifiedAt(dto.getLastModifiedAt())
                  .build();
  }

  public void update(CommentDto dto) {
    this.content = dto.getContent();
    this.lastModifiedAt = LocalDateTime.now();
  }

  @PrePersist
  public void registryTime() {
    this.registeredAt = LocalDateTime.now();
    this.lastModifiedAt = this.registeredAt;
  }

}
