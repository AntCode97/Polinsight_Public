package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @OneToOne(targetEntity = User.class)
  private User author;

  private String content;

  @ManyToOne
  @JoinColumn(name = "pno")
  private Post post;

  private LocalDateTime registeredAt;

  /*
   * 수정, 삭제 여부등을 저장할 필드
   * */
  private String status;

  @PrePersist
  public void registTime() {
    this.registeredAt = LocalDateTime.now();
  }

}
