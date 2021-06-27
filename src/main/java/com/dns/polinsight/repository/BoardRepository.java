package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>{
//    Board save(Board board);
//
//    Optional<Board> findById(Long id);
//    Optional<Board> findByContent(String searchContent);
//
//    Boolean delete(Board board);
//
//    List<Board> findAll();

    @Query(
            value = "SELECT b FROM Board b WHERE b.title LIKE %:title% AND b.boardType = :boardType",
            countQuery = "SELECT COUNT(b.id) FROM Board b WHERE b.title LIKE %:title% AND b.boardType = :boardType"
    )
    Page<Board> findByTitle(String title, BoardType boardType, Pageable pageable);

    @Query(
            value = "SELECT b FROM Board b WHERE b.searchcontent LIKE %:searchcontent% AND b.boardType = :boardType",
            countQuery = "SELECT COUNT(b.id) FROM Board b WHERE b.searchcontent LIKE %:searchcontent% AND b.boardType = :boardType"
    )
    Page<Board> findBySearchcontent(String searchcontent, BoardType boardType, Pageable pageable);

}
