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

  public static Phone of(String phone) {
    if (phone.isEmpty() || phone.length() < 11)
      return null;
    String[] arr = phone.split("-");
    return Phone.builder()
                .first(arr[0])
                .second(arr[1])
                .third(arr[2])
                .build();
  }

  @Override
  public String toString() {
    return first + "-" + second + "-" + third;
  }

}
