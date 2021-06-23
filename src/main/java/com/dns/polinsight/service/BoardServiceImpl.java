package com.dns.polinsight.service;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.exception.BoardNotFoundException;
import com.dns.polinsight.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;

  @Override
  public List<Board> findAll() {
    return repository.findAll();
  }


  public Board findOne(Long boardId){
    return repository.findById(boardId).orElseThrow(BoardNotFoundException::new);
  }

  @Override
  public Board find(Board board) {
    return repository.findById(board.getId()).orElseThrow(BoardNotFoundException::new);
  }

  @Override
  public Board saveOrUpdate(Board board) {
    return repository.save(board);
  }

  @Override
  public void delete(Board board) {
    repository.delete(board);
  }

}
