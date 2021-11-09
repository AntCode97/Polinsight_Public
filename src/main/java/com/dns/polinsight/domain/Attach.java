package com.dns.polinsight.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post.class)
  @JoinColumn(name = "pno")
  @NotNull
  private Post post;

  private Long fileSize;

  private String fileName;

  private String originalName;

  private String filePath;


}
