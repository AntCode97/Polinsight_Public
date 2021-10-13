package com.dns.polinsight.storage;

import com.dns.polinsight.exception.ImageResizeException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

  Stream<Path> loadAll();

  Path load(String filename);

  void delete(String filename) throws FileNotFoundException;

  Resource loadAsResource(String filename);

  void deleteAll();

  String store(String toString, MultipartFile thumbnailImg) throws IOException;

  String saveThumbnail(String uuid, MultipartFile thumbnail) throws ImageResizeException, TypeMismatchException;

}