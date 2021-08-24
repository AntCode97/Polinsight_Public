package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.PostType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
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

  private String content;

  private String viewcontent;

  @NotNull
  private User user;

  @CreatedDate
  private LocalDateTime registeredAt;

  private List<Attach> attaches;

  private List<MultipartFile> files;

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
  }

  public void transViewcontent() {
    this.viewcontent = this.content.replaceAll("\r\n", "<br>").replaceAll(" ", "&nbsp;");
  }

}
