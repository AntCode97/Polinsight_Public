package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.projection.PostMapping;
import com.dns.polinsight.types.PostType;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

  @Positive
  private Long id;

  @NotNull
  private String title;

  @NotNull
  private PostType postType;

  @NotNull
  private String content;

  private String viewcontent;

  @NotNull
  private User user;

  private String userName;

  private String userId;

  @CreatedDate
  private LocalDateTime registeredAt;

  private Set<Attach> attaches;

  private List<MultipartFile> files;

  private MultipartFile thumbnailImg;

  private Set<CommentDto> comments;

  private String thumbnail;

  @Builder.Default
  private Boolean isWriter = false;

  @Builder.Default
  private Long viewcnt = 0L;

  public PostDTO(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.postType = post.getPostType();
    this.viewcontent = post.getViewcontent().replaceAll("&nbsp;", " ").replaceAll("<br>", "\r\n");
    this.user = post.getUser();
    this.registeredAt = post.getRegisteredAt();
    this.attaches = post.getAttaches();
    this.viewcnt = post.getViewcnt();
    this.comments = post.getComments().stream().map(CommentDto::new).collect(Collectors.toSet());
    this.thumbnail = post.getThumbnail();
  }

  public static PostDTO of(Post post) {
    return PostDTO.builder()
                  .id(post.getId())
                  .content(post.getSearchcontent())
                  .user(post.getUser())
                  .postType(post.getPostType())
                  .title(post.getTitle())
                  .thumbnail(post.getThumbnail())
                  .attaches(post.getAttaches())
                  .userName(post.getUser().getName())
                  .userId(String.valueOf(post.getUser().getEmail()))
                  .registeredAt(post.getRegisteredAt())
                  .viewcontent(post.getViewcontent())
                  .comments(post.getComments().stream().map(CommentDto::new).collect(Collectors.toSet()))
                  .build();
  }

  public static PostDTO of(PostMapping mapping) {
    return PostDTO.builder()
                  .id(mapping.getId())
                  .title(mapping.getTitle())
                  .thumbnail(mapping.getThumbnail())
                  .postType(mapping.getPostType())
                  .user(mapping.getUser())
                  .userId(String.valueOf(mapping.getUser().getEmail()))
                  .userName(mapping.getUser().getName())
                  .registeredAt(mapping.getRegisteredAt())
                  .attaches(new TreeSet<>(mapping.getAttaches()))
                  .viewcnt(mapping.getViewCount())
                  .viewcontent(mapping.getViewcontent())
                  .comments(mapping.getComments().stream().map(CommentDto::new).collect(Collectors.toSet()))
                  .build();
  }

  public void transViewcontent() {
    this.viewcontent = this.content;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
