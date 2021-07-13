package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.BoardDTO;

import java.io.File;
import java.util.List;

public interface AttachService {

  List<Attach> findAll();

  void deleteAttaches(Long boardId);

  void delete(Attach attach);

  List<File> findFiles(Long boardId);

  Attach find(Attach attach);

  Attach saveOrUpdate(Attach attach);

  List<Attach> findByname(String filename);

  void addAttach(BoardDTO boardDTO);

}
