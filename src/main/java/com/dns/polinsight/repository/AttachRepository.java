package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Attach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {

  @Query(
      value = "SELECT a FROM Attach a WHERE a.post.id = :postId",
      countQuery = "SELECT COUNT(a.id) FROM Attach a WHERE a.post.id = :postId"
  )
  List<Attach> findByPostId(Long postId);

  List<Attach> findByFileName(String fileName);

}
