package com.dns.polinsight.repository;

import com.dns.polinsight.types.PostType;
import com.dns.polinsight.types.SearchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearch {

  private PostType postType;

  private SearchType searchType;

  private String searchValue;


}
