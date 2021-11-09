package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Post;
import com.dns.polinsight.projection.PostMapping;
import com.dns.polinsight.types.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(
      value = "SELECT b FROM Post b WHERE b.title LIKE %:title% AND b.postType = :postType",
      countQuery = "SELECT COUNT(b.id) FROM Post b WHERE b.title LIKE %:title% AND b.postType = :postType"
  )
  Page<Post> findByTitle(String title, PostType postType, Pageable pageable);

  @Query(
      value = "SELECT b FROM Post b WHERE b.searchcontent LIKE %:searchcontent% AND b.postType = :postType",
      countQuery = "SELECT COUNT(b.id) FROM Post b WHERE b.searchcontent LIKE %:searchcontent% AND b.postType = :postType"
  )
  Page<Post> findBySearchcontent(String searchcontent, PostType postType, Pageable pageable);

  Page<Post> findPostsBySearchcontentAndPostType(String searchcontent, PostType postType, Pageable pageable);

  @Query(
      value = "SELECT b FROM Post b WHERE b.searchcontent LIKE %:keyword% OR b.title LIKE %:keyword% ",
      countQuery = "SELECT COUNT(b.id) FROM Post b WHERE b.searchcontent LIKE %:keyword% OR b.title LIKE %:keyword%"
  )
  Page<Post> findBySearchKeyword(String keyword, Pageable pageable);

  @Transactional
  @Modifying()
  @Query(value = "UPDATE Post b SET b.viewcnt = b.viewcnt+1 WHERE b.id = :postId")
  void upViewCnt(Long postId);

  Page<PostMapping> findAllByPostType(PostType postType, Pageable pageable);

  Page<PostMapping> findPostMappingBySearchcontentContainingAndPostType(String searchcontent, PostType postType, Pageable pageable);

  Page<PostMapping> findPostMappingByTitleContainingAndPostType(String title, PostType postType, Pageable pageable);

  @Query(
      value = "SELECT b FROM Post b WHERE b.postType = :postType AND (b.searchcontent LIKE %:searchcontent% OR b.title LIKE %:title%)",
      countQuery = "SELECT COUNT(b.id) FROM Post b WHERE b.postType = :postType AND (b.searchcontent LIKE %:searchcontent% OR b.title LIKE %:title%)"
  )
  Page<PostMapping> findPostMappingBySearchcontentContainingOrTitleContainingAndPostType(String title, String searchcontent, PostType postType, Pageable pageable);


  @Query("select p from Post p where p.postType = :type and ( p.searchcontent like %:regex% or p.title like %:regex% or p.user.name like %:regex% )")
  Page<PostMapping> findAllByTypesAndRegex(PostType type, String regex, Pageable pageable);

  List<Post> findAllByPostType(PostType type);

}
