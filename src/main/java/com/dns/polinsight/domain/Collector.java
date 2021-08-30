package com.dns.polinsight.domain;

import com.dns.polinsight.types.CollectorStatusType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@ToString(exclude = "survey")
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

}
