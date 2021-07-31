package com.dns.polinsight.repository;

import com.dns.polinsight.types.BoardType;
import com.dns.polinsight.types.SearchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearch {

  private BoardType boardType;

  private SearchType searchType;

  private String searchValue;


}
