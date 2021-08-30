package com.dns.polinsight.domain;

import lombok.*;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@Setter
@Getter
public class PostStatus {


  private Long viewCount;


  //  @JoinColumn(name = "view_user")
  //  
  //  private List<User> viewer = new ArrayList<>();

  //  
  //  @Temporal(TemporalType.TIMESTAMP)
  //  private List<LocalDateTime> viewDate = new ArrayList<>();
}
