package com.dns.polinsight.domain;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

  @Id
  @Column(name = "bno")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private String searchcontent;
  private String viewcontent;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  @NotNull
  private User user;

  private LocalDateTime registeredAt;

  private String filePath;

  private BoardType boardType;

  public void update(String title, String content, LocalDateTime registeredAt){
    this.title = title;
    this.searchcontent = content;
    this.viewcontent = content;
    this.registeredAt =registeredAt;
  }


}
