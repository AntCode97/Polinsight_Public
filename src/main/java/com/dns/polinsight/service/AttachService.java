package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.ImageResizeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AttachService {

  List<Attach> findAll();

  void deleteAttaches(Long boardId);

  void delete(Attach attach);

  List<File> findFiles(Long boardId);

  List<MultipartFile> findMultipartFiles(Long boardId) throws IOException;

  Attach find(Attach attach);

  Attach saveOrUpdate(Attach attach);

  List<Attach> findByname(String filename);

  void addAttach(PostDTO postDTO) throws ImageResizeException;

  void deleteThumbnail(String thumbnailPath) ;

  String addAttach(MultipartFile file);

}
