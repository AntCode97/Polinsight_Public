package com.dns.polinsight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardStatus {

  @Id
  private Long id;

  private Long viewCount;


  @OneToMany(mappedBy = "id")
//  @JoinColumn(name = "view_user")
  @Builder.Default
  private List<User> viewer = new ArrayList<>();
  // TODO: 2021-07-24
  //  @Builder.Default
  //  @Temporal(TemporalType.TIMESTAMP)
  //  private List<LocalDateTime> viewDate = new ArrayList<>();
}
