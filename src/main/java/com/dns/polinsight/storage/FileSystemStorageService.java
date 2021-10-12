package com.dns.polinsight.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;


  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
    this.init();
  }

  @Override
  public String store(String uuid, MultipartFile file) {
    String fileName = null;
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file.");
      }
      fileName = uuid + file.getOriginalFilename();
      Path destinationFile = this.rootLocation.resolve(
                                     Paths.get(uuid + file.getOriginalFilename()))
                                              .normalize().toAbsolutePath();
      if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
        // This is a security check
        throw new StorageException(
            "Cannot store file outside current directory.");
      }

      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile,
            StandardCopyOption.REPLACE_EXISTING);
      }

    } catch (IOException e) {
      throw new StorageException("Failed to store file.", e);
    }
    return fileName;
  }

  @Override
  public void store(MultipartFile file) {
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file.");
      }
      Path destinationFile = this.rootLocation.resolve(
                                     Paths.get(file.getOriginalFilename()))
                                              .normalize().toAbsolutePath();
      if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
        // This is a security check
        throw new StorageException(
            "Cannot store file outside current directory.");
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile,
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file.", e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.rootLocation, 1)
                  .filter(path -> !path.equals(this.rootLocation))
                  .map(this.rootLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public void deleteThumbnail(String thumbnailPath) {
    Path path = Paths.get(String.valueOf(rootLocation), thumbnailPath);
    this.delete(path.toString());
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void delete(String filepath) {
    //            Path destinationFile = this.rootLocation.resolve(
    //                    Paths.get(filename)
    //                    .normalize().toAbsolutePath());
    //            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
    //                // This is a security check
    //                throw new StorageException(
    //                        "Cannot store file outside current directory.");
    //            }
    File file = new File(filepath);
    if (file.delete()) {
      System.out.println("파일 삭제 성공");
    } else {
      System.out.println("파일 삭제 실패");
    }


  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  @Override
  public void init() {
    try {
      if (!Files.exists(rootLocation))
        Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

}