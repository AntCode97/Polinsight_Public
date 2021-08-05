package com.dns.polinsight.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePwdDto {

  @Id
  private String email;

  @NotNull
  private String name;

  @NotNull
  private String hash;

}
