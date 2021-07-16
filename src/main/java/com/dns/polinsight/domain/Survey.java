package com.dns.polinsight.domain;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Survey implements Serializable {

  private static final long serialVersionUID = -4701183897615758658L;

  @Id
  private String id;

  private Long surveyId;

  private String title;

  private String nickname;

  private String href;

  /*
   * 설문 완료시 지급할 포인트 수치
   * */
  private Long point;

}
