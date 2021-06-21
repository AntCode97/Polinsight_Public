package com.dns.polinsight.service;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;

  @Override
  public List<Board> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Board> find(Board board) {
    return repository.findById(board.getId());
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
