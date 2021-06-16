package com.dns.polinsight.service;

import com.dns.polinsight.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardService {

  List<Board> findAll();

  Optional<Board> find(Board board);

  Board saveOrUpdate(Board board);

  void delete(Board board);

}
