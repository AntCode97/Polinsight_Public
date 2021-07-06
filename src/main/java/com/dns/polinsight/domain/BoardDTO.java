package com.dns.polinsight.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

  @Positive
  private Long id;

  private String title;

  @NotNull
  private BoardType boardType;

  private String content;

  private String viewcontent;

  @NotNull
  private User user;


  private LocalDateTime registeredAt;

  private List<Attach> attaches;

  private List<MultipartFile> files;

  public void transViewcontent() {

    String viewcontent = this.content.replace("\r\n", "<br>");
    viewcontent = viewcontent.replace(" ", "&nbsp;");
    this.viewcontent = viewcontent;
  }

}
