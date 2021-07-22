package com.dns.polinsight.domain;

import com.dns.polinsight.types.ProgressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SurveyStatus implements Serializable {

  private static final long serialVersionUID = 4634709298238240835L;

  @Id
  private Long id;

  @Builder.Default
  private Long count = 0L;

  private ProgressType progressType = ProgressType.BEFORE;

  @OneToOne(mappedBy = "status")
  @Setter
  private Survey survey;


  public void setProgressType(LocalDateTime endDateTime) {
    int cmp = LocalDateTime.now().compareTo(endDateTime);
    if (cmp < 0) {
      this.progressType = ProgressType.ONGOING;
    } else {
      this.progressType = ProgressType.END;
    }
  }

}
