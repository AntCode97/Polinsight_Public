package com.dns.polinsight.domain.dto;

import java.util.List;

public class ListDto<T> {

  List<T> list;

  public ListDto(List<T> list) {
    this.list = list;
  }

}
