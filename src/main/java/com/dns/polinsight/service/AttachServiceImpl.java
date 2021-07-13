package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardDTO;
import com.dns.polinsight.exception.AttachNotFoundException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.BoardRepository;
import com.dns.polinsight.storage.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService {

  private final AttachRepository repository;

  private final BoardRepository boardRepository;

  private final FileSystemStorageService storageService;

  private final BoardService boardService;

  @Value("${file.upload.baseLocation}")
  private String baseLocation;

  @Override
  public List<Attach> findAll() {
    return repository.findAll();
  }

  @Override
  public void deleteAttaches(Long boardId) {
    repository.findByBoardId(boardId).forEach(attach -> {
      storageService.delete(attach.getFilePath());
      repository.delete(attach);
    });
  }

  @Override
  public void delete(Attach attach) {
    storageService.delete(attach.getFilePath());
    repository.delete(attach);
  }

  @Override
  public List<File> findFiles(Long boardId) {
    //    Board board = boardRepository.findById(boardId).get();
    //    System.out.println(board.getId() + board.getTitle());
    return repository.findByBoardId(boardId).stream().map(attach -> new File(attach.getFilePath())).collect(Collectors.toList());
  }


  @Override
  public Attach find(Attach attach) {
    return repository.findById(attach.getId()).orElseThrow(AttachNotFoundException::new);
  }

  @Override
  public Attach saveOrUpdate(Attach attach) {
    return repository.save(attach);
  }

  @Override
  public List<Attach> findByname(String filename) {
    return repository.findByFilename(filename);
  }

  @Override
  public void addAttach(BoardDTO boardDTO) {
    List<MultipartFile> files = boardDTO.getFiles();
    if (!files.isEmpty()) {
      List<Attach> attaches = new ArrayList<>();
      for (MultipartFile file : files) {
        if (!file.isEmpty()) {
          UUID uuid = UUID.randomUUID();
          Attach attach = Attach.builder()
                                .filename(uuid + file.getOriginalFilename())
                                .fileSize(file.getSize())
                                .originalName(file.getOriginalFilename())
                                .filePath(baseLocation + uuid + file.getOriginalFilename())
                                .board(Board.builder(boardDTO).build())
                                .build();
          attaches.add(attach);
          storageService.store(uuid.toString(), file);
        }
      }
      repository.saveAll(attaches);
      boardDTO.setAttaches(attaches);
      boardService.addBoard(boardDTO);
    }
  }

}
