package com.dns.polinsight.domain;

import com.dns.polinsight.projection.SurveyMapping;
import com.dns.polinsight.types.CollectorStatusType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Collector implements Serializable {

  private static final long serialVersionUID = 4837730403775276600L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long collectorId;

  private String name;

  private String participateUrl;

  private String href;

  private Long responseCount;

  @JsonBackReference
  @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")
  @OneToOne
  private Survey survey;


  @Enumerated(EnumType.STRING)
  private CollectorStatusType status;

  public static Collector of(SurveyMapping mapping) {
    return Collector.builder()
                    .id(mapping.getCollectorId())
                    .collectorId(mapping.getCollectorCollectorId())
                    .name(mapping.getCollectorName())
                    .href(mapping.getCollectorHref())
                    .participateUrl(mapping.getCollectorParticipateUrl())
                    .responseCount(mapping.getCollectorResponseCount())
                    .status(mapping.getCollectorStatus())
                    .build();
  }

  @Override
  public String toString() {
    ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.JSON_STYLE);
    return ReflectionToStringBuilder.toStringExclude(this, "survey");
  }

}
