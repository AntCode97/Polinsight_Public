package com.dns.polinsight.service;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardDTO;
import com.dns.polinsight.domain.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

  List<Board> findAll();

  Board findOne(Long boardId);

  Board find(Board board);

  void delete(Board board);

  Board addBoard(BoardDTO boardDTO);

  void renewBoard();

  Page<Board> getBoardList(Pageable pageable);

  Page<Board> searchTitle(String title, BoardType boardType, Pageable pageable);

  Page<Board> searchContent(String searchcontent, BoardType boardType, Pageable pageable);

}
