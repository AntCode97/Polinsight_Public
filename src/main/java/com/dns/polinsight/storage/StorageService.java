package com.dns.polinsight.storage;

import com.dns.polinsight.exception.ImageResizeException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface StorageService {

  void delete(String filename) throws FileNotFoundException;

  void store(String toString, MultipartFile thumbnailImg) throws IOException;

  String saveThumbnail(String uuid, MultipartFile thumbnail) throws ImageResizeException, TypeMismatchException;

}