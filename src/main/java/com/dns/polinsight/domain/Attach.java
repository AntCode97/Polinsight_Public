package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attach {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bno")
  @NotNull
  private Board board;

  private Long fileSize;

  private String fileName;

  private String originalName;

  private String filePath;


}
