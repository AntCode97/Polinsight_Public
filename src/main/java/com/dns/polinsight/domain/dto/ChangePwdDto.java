package com.dns.polinsight.domain.dto;

import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.convereter.EmailAttrConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePwdDto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Convert(converter = EmailAttrConverter.class, attributeName = "email")
  private Email email;

  @NotNull
  private String name;

  @NotNull
  private String hash;

}
