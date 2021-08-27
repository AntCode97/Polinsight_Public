package com.dns.polinsight.domain;

import com.dns.polinsight.types.ProgressType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
public class SurveyStatus {


  // 서베이몽키에서 등록한 설문의 커스텀 변수
  @ElementCollection
  @Column(name = "variable")
  @Setter
  private Set<String> variables = new HashSet<>();

  // 설문을 완료한 사람 수
  @Setter
  private Long count;

  @Enumerated(EnumType.STRING)
  @Setter
  private ProgressType progress = ProgressType.BEFORE;

  private Integer minimumTime = 30;


  public void setProgressByDate(LocalDateTime endDateTime) {
    try {
      int cmp = LocalDateTime.now().compareTo(endDateTime);
      if (cmp < 0) {
        this.progress = ProgressType.ONGOING;
      } else {
        this.progress = ProgressType.END;
      }
    } catch (Exception e) {
      this.progress = ProgressType.BEFORE;
    }
  }

}
