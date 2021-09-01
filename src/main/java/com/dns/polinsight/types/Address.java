package com.dns.polinsight.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

  private static final long serialVersionUID = -6145234369745563876L;

  private String state;

  private String city;

  public static Address of(String address) {
    if (address == null || address.isBlank() || address.isEmpty())
      return null;
    String[] arr = address.split(" ");
    return Address.builder()
                  .state(arr[0])
                  .city(arr[1])
                  .build();
  }

  @Override
  public String toString() {
    return state + " " + city;
  }

}
