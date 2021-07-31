package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.types.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {
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

  @Query(
          value = "SELECT b FROM Board b WHERE b.searchcontent LIKE %:keyword% OR b.title LIKE %:keyword% ",
          countQuery = "SELECT COUNT(b.id) FROM Board b WHERE b.searchcontent LIKE %:keyword% OR b.title LIKE %:keyword%"
  )
  Page<Board> findBySearchKeyword(String keyword,Pageable pageable);

  @Transactional
  @Modifying()
  @Query(
    value = "UPDATE Board b SET b.viewcnt = :#{#board.viewcnt}+1 WHERE b.id = :#{#board.id}"
          )
  void upViewCnt(Board board);

}
