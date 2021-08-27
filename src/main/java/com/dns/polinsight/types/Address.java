package com.dns.polinsight.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

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
