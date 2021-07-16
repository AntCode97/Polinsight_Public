package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Survey;
import lombok.*;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyMonkeyDTO {

  private Long id;

  private String title;

  private String nickname;

  private String href;

  public Survey toSurvey() {
    return Survey.builder()
                 .surveyId(this.id)
                 .title(this.title)
                 .nickname(this.nickname)
                 .href(this.href)
                 .build();
  }

}
