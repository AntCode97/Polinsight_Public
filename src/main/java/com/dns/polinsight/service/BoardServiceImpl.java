package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.dto.BoardDTO;
import com.dns.polinsight.types.BoardType;
import com.dns.polinsight.exception.BoardNotFoundException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;

  private final AttachRepository attachRepository;

  @Cacheable
  @Override
  public List<Board> findAll() {
    return repository.findAll();
  }


  @Override
  public Board findOne(Long boardId) {
    return repository.findById(boardId).orElseThrow(BoardNotFoundException::new);
  }

  @Override
  public Board find(Board board) {
    return repository.findById(board.getId()).orElseThrow(BoardNotFoundException::new);
  }


  @Override
  public Board addBoard(BoardDTO boardDTO) {
    return repository.save(Board.builder(boardDTO).build());
  }

  @Transactional
  @Override
  public void renewBoard() {
    List<Board> boards = this.findAll();
    for (Board board : boards) {
      LocalDateTime writeTime = board.getRegisteredAt();
      LocalDateTime now = LocalDateTime.now();
      Duration duration = Duration.between(writeTime, now);
      board.setNewBoard(duration.getSeconds() < 3600 * 12);
    }
  }

  @Override
  public void delete(Board board) {
    List<Attach> attaches = attachRepository.findByBoardId(board.getId());
    attaches.forEach(attachRepository::delete);
    repository.delete(board);
  }


  public Page<Board> getBoardList(Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id"); // <- Sort 추가

    return repository.findAll(pageable);
  }

  @Override
  public Page<Board> searchTitle(String title, BoardType boardType, Pageable pageable) {
    return repository.findByTitle(title, boardType, pageable);

  }

  @Override
  public Page<Board> searchContent(String searchcontent, BoardType boardType, Pageable pageable) {
    return repository.findBySearchcontent(searchcontent, boardType, pageable);

  }

}
