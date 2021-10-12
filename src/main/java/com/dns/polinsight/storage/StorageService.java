package com.dns.polinsight.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

  void init();

  void store(MultipartFile file);

  Stream<Path> loadAll();

  Path load(String filename);

  void delete(String filename);

  void deleteThumbnail(String thumbnailPath);

  Resource loadAsResource(String filename);

  void deleteAll();

  String store(String toString, MultipartFile thumbnailImg);
}