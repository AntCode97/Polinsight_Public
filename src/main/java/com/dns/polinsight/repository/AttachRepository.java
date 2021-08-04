package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Attach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {

  @Query(
      value = "SELECT a FROM Attach a WHERE a.board.id = :boardId",
      countQuery = "SELECT COUNT(a.id) FROM Attach a WHERE a.board.id = :boardId"
  )
  List<Attach> findByBoardId(Long boardId);

  List<Attach> findByFileName(String fileName);

}
