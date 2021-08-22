package com.dns.polinsight.object;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.types.PostType;

import java.util.List;

public interface PostVO {

  Long getNum();

  Long getId();

  String getTitle();

  PostType getPostType();

  String getContent();

  String getViewContent();

  String getUser();

  String getRegisteredAt();

  List<Attach> getAttaches();

  Long getCount();

}
