package com.dns.polinsight.domain;

import com.dns.polinsight.types.SurveyProgressType;
import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

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

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  private LocalDateTime endAt;

  private String preview;

  private Long point;

  private SurveyProgressType progressType;

}
