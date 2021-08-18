package com.dns.polinsight.domain;

import com.dns.polinsight.types.CollectorStatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collector {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long collectorId;

  private String name;

  private String href;


  private String participateUrl;


  private Long responseCount;

  @JsonIgnore
  @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")
  @ManyToOne
  private Survey survey;


  @Enumerated(EnumType.STRING)
  private CollectorStatusType status;

}
