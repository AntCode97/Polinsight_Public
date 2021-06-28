package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {
    @Query(
            value = "SELECT a FROM Attach a WHERE a.board.id = :boardId",
            countQuery = "SELECT COUNT(a.id) FROM Attach a WHERE a.board.id = :boardId"
    )
    List<Attach> findByBoardId(Long boardId);
}
