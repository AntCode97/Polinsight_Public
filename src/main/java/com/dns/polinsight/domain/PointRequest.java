package com.dns.polinsight.domain;

import com.dns.polinsight.types.BankType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 사용자가 포인트를 현금화 시키고자 할 때, 사용할 테이블
 * */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest implements Serializable {

  private static final long serialVersionUID = 1793106791023567213L;

  @Id
  private Long id;

  private Long uid;

  private Long requestPoint;

  private LocalDateTime requestedAt;

  // 은행 타입
  private BankType bankName;

  //  유저가 요청한 계좌
  private String account;

}
