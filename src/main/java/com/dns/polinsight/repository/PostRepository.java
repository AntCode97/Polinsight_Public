package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Post;
import com.dns.polinsight.object.PostVO;
import com.dns.polinsight.types.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {
  //    Post save(Post post);
  //
  //    Optional<Post> findById(Long id);
  //    Optional<Post> findByContent(String searchContent);
  //
  //    Boolean delete(Post post);
  //
  //    List<Post> findAll();

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

  @Query(
      value = "SELECT b FROM Post b WHERE b.searchcontent LIKE %:keyword% OR b.title LIKE %:keyword% ",
      countQuery = "SELECT COUNT(b.id) FROM Post b WHERE b.searchcontent LIKE %:keyword% OR b.title LIKE %:keyword%"
  )
  Page<Post> findBySearchKeyword(String keyword, Pageable pageable);

  @Transactional
  @Modifying()
  @Query(
      value = "UPDATE Post b SET b.viewcnt = b.viewcnt+1 WHERE b.id = :postId"
  )
  void upViewCnt(Long postId);

  @Query(nativeQuery = true, value = "SELECT ROW_NUMBER() OVER () AS num, p.pno AS id, p.title AS title, p.type AS post_type, p.search_content AS content," +
      "p.view_content AS view_content, u.name AS user, p.registered_at AS registered_at, p.view_count AS count FROM post p " +
      "JOIN user u ON p.user_id = u.id WHERE p.type LIKE ?1")
  Page<PostVO> findPostsByType(String postType, Pageable pageable);

}
