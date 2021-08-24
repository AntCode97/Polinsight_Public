package com.dns.polinsight.service;

import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.mapper.PostMapping;
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

  Page<PostMapping> findPostsByType(PostType type, Pageable pageable);

  Page<PostMapping> findPostsByTitle(String title, PostType type, Pageable pageable);

  Page<PostMapping> findPostsBySearchcontent(String searchcontent, PostType type, Pageable pageable);

  Page<PostMapping> findBySearchKeyword(String keyword, PostType postType, Pageable pageable);

  Page<Post> getPostList(Pageable pageable);

  Page<Post> searchTitle(String title, PostType postType, Pageable pageable);

  Page<Post> searchContent(String searchcontent, PostType postType, Pageable pageable);

  Page<Post> searchKeyword(String keyword, Pageable pageable);

  void upViewCnt(Long postId);

}
