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
public class Address implements Serializable {

  private String state;

  private String city;

  @Override
  public String toString() {
    return state + " " + city;
  }

}
