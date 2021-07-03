package com.dns.polinsight.service;

import com.dns.polinsight.domain.Board;

import java.util.List;

public interface BoardService {

  List<Board> findAll();

  Board find(Board board);

//  Board saveOrUpdate(Board board);

  void delete(Board board);

}
