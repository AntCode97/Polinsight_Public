package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardDTO;
import com.dns.polinsight.domain.BoardType;
import com.dns.polinsight.exception.BoardNotFoundException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;
  private final AttachRepository attachRepository;

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


  public Board addBoard(BoardDTO boardDTO) {
    Board board = Board.builder(boardDTO).build();

    return repository.save(board);
  }

//  @Override
//  public Board saveOrUpdate(Board board) {
//    return repository.save(board);
//  }


  @Transactional
  public Long update(Long id, BoardDTO boardDTO){
    Board board = repository.findById(id).orElseThrow(BoardNotFoundException::new);
    board.update(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getRegisteredAt());
    return board.getId();


  }

  @Override
  public void delete(Board board) {
    List<Attach> attaches = attachRepository.findByBoardId(board.getId());
    for (Attach attach:
         attaches) {
      attachRepository.delete(attach);
    }

    repository.delete(board);
  }

//  public Page<Board> getBoardList(Pageable pageable) {
//    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
//    pageable = PageRequest.of(page, 10, new Sort(Sort.Direction.DESC, "id"));
//
//    return repository.findAll(pageable);
//  }
  public Page<Board> getBoardList(Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.by("id")); // <- Sort 추가

    return repository.findAll(pageable);
  }

  public Page<Board> searchTitle(String title, BoardType boardType, Pageable pageable){

    Page<Board> boards = repository.findByTitle(title, boardType, pageable);
    return boards;

  };

  public Page<Board> searchContent(String searchcontent,BoardType boardType, Pageable pageable){

    Page<Board> boards = repository.findBySearchcontent(searchcontent,boardType, pageable);
    return boards;

  };

}
