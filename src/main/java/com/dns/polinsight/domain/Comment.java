package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long seq;

  @Setter
  @JsonIgnore
  @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User writer;

  @Column(nullable = false)
  private String content;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "pno")
  private Post post;

  private LocalDateTime registeredAt;

  private LocalDateTime lastModifiedAt;

  /**
   * 부모 댓글의 번호
   */
  private Long parent;

  private Long depth;

  /*
   * 수정, 삭제 여부등을 저장할 필드
   * */
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
