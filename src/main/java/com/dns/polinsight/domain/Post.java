package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.types.PostType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Builder(builderMethodName = "PostBuilder")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post implements Serializable {

  private static final long serialVersionUID = 5170758413872517587L;

  @OneToMany(mappedBy = "post")
  @Builder.Default
  @JsonIgnore
  private final List<Attach> attaches = new ArrayList<>();

  @Id
  @Column(name = "pno")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @PositiveOrZero
  private Long id;

  @NotEmpty
  private String title;

  @NotEmpty
  private String searchcontent;

  @NotEmpty
  private String viewcontent;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @NotNull
  private User user;

  private LocalDateTime registeredAt;

  @Enumerated(EnumType.STRING)
  private PostType postType;

  private Boolean newPost;

  @Setter
  private long viewcnt;

  public static PostBuilder builder(PostDTO postDTO) {
    return PostBuilder()
        .id(postDTO.getId())
        .title(postDTO.getTitle())
        .searchcontent(postDTO.getContent())
        .viewcontent(postDTO.getViewcontent())
        .user(postDTO.getUser())
        .registeredAt(postDTO.getRegisteredAt())
        .postType(postDTO.getPostType())
        .attaches(postDTO.getAttaches())
        .viewcnt(0);
  }


  //  //TODO: 게시글 업데이트도 포함해야함
  //  public void update(String title, String content, LocalDateTime registeredAt){
  //    this.title = title;
  //    this.searchcontent = content;
  //    this.viewcontent = content;
  //    this.registeredAt =registeredAt;
  //  }

  public void setNewPost(Boolean time) {
    this.newPost = time;
  }


}
