package com.dns.polinsight.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Serializable {

  private String first;

  private String second;

  private String third;

  @Override
  public String toString() {
    return first + "-" + second + "-" + third;
  }

}
