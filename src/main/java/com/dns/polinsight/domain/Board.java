package com.dns.polinsight.domain;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder(builderMethodName = "BoardBuilder")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

  @Id
  @Column(name = "bno")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private String searchcontent;
  private String viewcontent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @NotNull
  private User user;

  private LocalDateTime registeredAt;

  private BoardType boardType;

  @OneToMany(mappedBy = "board") //누구에 의해서 매핑되는가,
  private List<Attach> attaches = new ArrayList<>();

  private Boolean newBoard;

  public static BoardBuilder builder(BoardDTO boardDTO){
    return BoardBuilder()
    .id(boardDTO.getId())
            .title(boardDTO.getTitle())
            .searchcontent(boardDTO.getContent())
            .viewcontent(boardDTO.getViewcontent())
            .user(boardDTO.getUser())
            .registeredAt(boardDTO.getRegisteredAt())
            .boardType(boardDTO.getBoardType())
            .attaches(boardDTO.getAttaches());
  }



//  //TODO: 게시글 업데이트도 포함해야함
//  public void update(String title, String content, LocalDateTime registeredAt){
//    this.title = title;
//    this.searchcontent = content;
//    this.viewcontent = content;
//    this.registeredAt =registeredAt;
//  }

  public void setNewBoard(Boolean time){
    if(time){
      this.newBoard = true;
    }
    else
    {
      this.newBoard = false;
    }
  }



}
