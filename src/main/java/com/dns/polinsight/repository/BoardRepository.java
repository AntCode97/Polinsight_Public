package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
