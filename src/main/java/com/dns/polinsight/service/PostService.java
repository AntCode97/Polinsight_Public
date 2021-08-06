package com.dns.polinsight.service;

import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.types.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

  List<Post> findAll();

  Post findOne(Long postId);

  Post find(Post post);

  void delete(Post post);

  Post addPost(PostDTO postDTO);

  void renewPost();

  Page<Post> getPostList(Pageable pageable);

  Page<Post> searchTitle(String title, PostType postType, Pageable pageable);

  Page<Post> searchContent(String searchcontent, PostType postType, Pageable pageable);

  Page<Post> searchKeyword(String keyword, Pageable pageable);

  void upViewCnt(Post post);

}
