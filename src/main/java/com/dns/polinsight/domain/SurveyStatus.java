package com.dns.polinsight.domain;

import com.dns.polinsight.exception.InvalidValueException;
import com.dns.polinsight.projection.SurveyMapping;
import com.dns.polinsight.types.ProgressType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
public class SurveyStatus implements Serializable {

  private static final long serialVersionUID = 4278696733416424086L;

  // 서베이몽키에서 등록한 설문의 커스텀 변수
  @Builder.Default
  @ElementCollection
  @Column(name = "variable")
  @Setter
  private Set<String> variables = new HashSet<>();

  // 설문을 완료한 사람 수
  @Setter
  private Long count;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Setter
  private ProgressType progress = ProgressType.BEFORE;

  @Builder.Default
  private Integer minimumTime = 30;

  public static SurveyStatus of(SurveyMapping mapping) {
    return SurveyStatus.builder()
                       .variables(mapping.getVariables())
                       .count(mapping.getCount())
                       .progress(mapping.getProgress())
                       .minimumTime(mapping.getMinimumTime())
                       .build();
  }

  public void setProgressByDate(LocalDateTime endDateTime) {
    try {
      int cmp = LocalDateTime.now().compareTo(endDateTime);
      if (cmp < 0) {
        this.progress = ProgressType.ONGOING;
      } else {
        this.progress = ProgressType.END;
      }
    } catch (InvalidValueException e) {
      this.progress = ProgressType.BEFORE;
    }
  }

}
