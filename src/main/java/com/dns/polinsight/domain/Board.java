package com.dns.polinsight.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(builderMethodName = "BoardBuilder")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

  @OneToMany(mappedBy = "board") //누구에 의해서 매핑되는가,
  private List<Attach> attaches = new ArrayList<>();

  @Id
  @Column(name = "bno")
  @GeneratedValue(strategy = GenerationType.AUTO)
  @PositiveOrZero
  private Long id;

  @NotEmpty
  private String title;

  @NotEmpty
  private String searchcontent;

  @NotEmpty
  private String viewcontent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @NotNull
  private User user;

  private LocalDateTime registeredAt;

  private BoardType boardType;

  private Boolean newBoard;

  public static BoardBuilder builder(BoardDTO boardDTO) {
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

  public void setNewBoard(Boolean time) {
    this.newBoard = time;
  }


}
