package com.dns.polinsight.domain.dto;

import com.dns.polinsight.domain.Survey;
import com.dns.polinsight.domain.SurveyStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyMonkeyDTO {

  private Long id;

  private String title;

  private LocalDateTime createdAt;

  private String preview;

  private String nickname;

  private String language;

  private String folder_id;

  private String category;

  private String question_count;

  private String page_count;

  private String response_count;

  private String date_created;

  private String date_modified;


  private String is_owner;

  private String footer;

  private Map<String, String> custom_variables;

  private String href;

  private String analyze_url;

  private String edit_url;

  private String collect_url;

  private String summary_url;

  private String url;

  private Long total;

  public Survey toSurvey() {
    return Survey.builder()
                 .surveyId(this.id)
                 .title(this.title)
                 .status(new SurveyStatus())
                 .createdAt(this.createdAt)
                 .build();
  }

}
