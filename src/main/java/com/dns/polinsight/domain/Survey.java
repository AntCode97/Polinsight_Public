package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.SurveyMonkeyDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Survey implements Serializable {

  private static final long serialVersionUID = -4701183897615758658L;

//  @OneToOne(fetch = FetchType.EAGER)
  @Setter
  @Embedded
  private SurveyStatus status;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 서베이 몽키에서 제공하는 설문 아이디
  private Long surveyId;

  //  private String nickname;

  //  private String href;

  private String title;

  //  private LocalDateTime modifiedAt;

  @Setter
  private LocalDateTime createdAt;

  //  private String preview;

  private LocalDateTime endAt;

  private Long point;

}
