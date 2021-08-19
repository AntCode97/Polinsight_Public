package com.dns.polinsight.domain;

import com.dns.polinsight.types.CollectorStatusType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@ToString(exclude = "survey")
public class Collector {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long collectorId;

  private String name;

  private String participateUrl;

  private String href;

  private Long responseCount;

  @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")
  @ManyToOne
  private Survey survey;


  @Enumerated(EnumType.STRING)
  private CollectorStatusType status;

}
