package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Survey;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyMonkeyDTO {

  private Long id;

  private String title;

  //  private String nickname;

  //  private String href;

  private LocalDateTime createdAt;

  //  private LocalDateTime modifiedAt;

  private String preview;

  public Survey toSurvey() {
    return Survey.builder()
                 .surveyId(this.id)
                 .title(this.title)
                 //                 .nickname(this.nickname)
                 //                 .href(this.href)
                 .createdAt(this.createdAt)
                 //                 .modifiedAt(this.modifiedAt)
                 //                 .preview(this.preview)
                 .build();
  }

}
