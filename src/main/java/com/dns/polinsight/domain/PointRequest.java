package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.PointRequestDto;
import com.dns.polinsight.types.BankType;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.PointRequestProgressType;
import com.dns.polinsight.types.convereter.EmailAttrConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  private Long uid;

  @Convert(converter = EmailAttrConverter.class, attributeName = "email")
  private Email email;

  private Long requestPoint;

  @Builder.Default
  private LocalDateTime requestedAt = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  private BankType bank;

  private String account;

  private String name;

  @Setter
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private PointRequestProgressType progress = PointRequestProgressType.REQUESTED;

  public PointRequest of(PointRequestDto dto) {
    this.requestedAt = dto.getRequestedAt();
    this.bank = dto.getBank();
    this.account = dto.getAccount();
    this.progress = dto.getProgress();
    return this;
  }

  public PointRequest progressUpdate(PointRequestDto dto) {
    this.progress = dto.getProgress();
    return this;
  }

}
