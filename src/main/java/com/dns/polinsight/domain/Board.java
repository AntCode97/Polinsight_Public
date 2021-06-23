package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private User user;

  private LocalDateTime registeredAt;

}
