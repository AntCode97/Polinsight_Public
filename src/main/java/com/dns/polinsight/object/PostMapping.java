package com.dns.polinsight.object;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.PostType;
import com.dns.polinsight.types.ProgressType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface PostMapping {

  Long getId();

  String getTitle();

  PostType getPostType();

  default String getContent() {
    return getSearchcontent();
  }

  @JsonIgnore
  String getSearchcontent();

  String getViewcontent();

  default String getName() {
    return getUser().getName();
  }


  @JsonIgnore
  User getUser();

  default String getRegistedAt() {
    return getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  @JsonIgnore
  LocalDateTime getRegisteredAt();

  List<Attach> getAttaches();

  default Long getViewCount() {
    return getViewcnt();
  }

  @JsonIgnore
  Long getViewcnt();

}
