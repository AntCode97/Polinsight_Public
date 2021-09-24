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

@Table(name = "Post")
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

  private String thumbnail;

  @Id
  @Column(name = "pno")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @PositiveOrZero
  private Long id;

  @NotEmpty
  private String title;

  @NotEmpty
  @Column(name = "search_content", length = 3000)
  private String searchcontent;

  @NotEmpty
  @Column(name = "view_content", length = 3000)
  private String viewcontent;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @NotNull
  private User user;

  private LocalDateTime registeredAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private PostType postType;

  @Setter
  @Column(name = "view_count")
  private Long viewcnt;


  @OneToMany(mappedBy = "post", targetEntity = Comment.class, cascade = CascadeType.ALL)
  private List<Comment> comments;

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
        .viewcnt(postDTO.getViewcnt())
        .thumbnail(postDTO.getThumbnail());
  }


}
