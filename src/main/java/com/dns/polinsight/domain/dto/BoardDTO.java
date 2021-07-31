package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.BoardType;
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
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

  @Positive
  private Long id;

  @NotNull
  private String title;

  @NotNull
  private BoardType boardType;

  private String content;

  private String viewcontent;

  @NotNull
  private User user;

  @CreatedDate
  private LocalDateTime registeredAt;

  private List<Attach> attaches;

  private List<MultipartFile> files;


  public void transViewcontent() {
    this.viewcontent = this.content.replaceAll("\r\n", "<br>").replaceAll(" ", "&nbsp;");
  }

}
