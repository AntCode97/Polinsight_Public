package com.dns.polinsight.storage;

import com.dns.polinsight.exception.ImageResizeException;
import com.dns.polinsight.utils.ImageUtil;
import com.dns.polinsight.utils.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.InputMismatchException;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  private final TypeCheckUtil typeCheckUtil;

  private final ImageUtil imageUtil;

  @Value("${file.upload.image}")
  private String imageLocation;

  @Value("${file.upload.file}")
  private String fileLocation;

  @Autowired
  public FileSystemStorageService(StorageProperties properties, TypeCheckUtil typeCheckUtil, ImageUtil imageUtil) {
    this.rootLocation = Paths.get(properties.getLocation());
    this.typeCheckUtil = typeCheckUtil;
    this.imageUtil = imageUtil;
  }

  // TODO: 2021-10-15 비동기 처리 필요
  @Override
  public void store(String uuid, MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new StorageException("Failed to store empty file.");
    }
    Path destinationFile = getPathByType(file, uuid);
    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  @Override
  public String saveThumbnail(String uuid, MultipartFile thumbnail) throws ImageResizeException, TypeMismatchException {
    if (!typeCheckUtil.isImageFile(thumbnail.getOriginalFilename()))
      throw new InputMismatchException("thumbnail must be image");
    return imageUtil.imageResize(thumbnail,
        uuid + thumbnail.getOriginalFilename(),
        typeCheckUtil.getImageFileExt(thumbnail.getOriginalFilename()));
  }

    @Override
    public Path load(String filename) {

      if (typeCheckUtil.isImageFile(filename))
        return rootLocation.resolve(Paths.get(imageLocation + filename));
      else
        return rootLocation.resolve(Paths.get(fileLocation + filename));
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
  public void delete(String filepath) throws FileNotFoundException {
    try {
      File file = new File(Paths.get(filepath).toString());
      file.delete();
      log.info(file.getName() + " has deleted");
    } catch (SecurityException se) {
      throw new SecurityException(se.getMessage());
    }
  }


  @PostConstruct
  public void init() {
    try {
      if (!Files.exists(rootLocation))
        Files.createDirectories(rootLocation);
      if (!Files.exists(Paths.get(imageLocation)))
        Files.createDirectories(Paths.get(imageLocation));
      if (!Files.exists(Paths.get(fileLocation)))
        Files.createDirectories(Paths.get(fileLocation));

    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }


  private Path getPathByType(MultipartFile file, String uuid) {
    if (typeCheckUtil.isImageFile(file.getOriginalFilename()))
      return rootLocation.resolve(Paths.get(imageLocation + uuid + file.getOriginalFilename())).normalize().toAbsolutePath();
    else
      return rootLocation.resolve(Paths.get(fileLocation + uuid + file.getOriginalFilename())).normalize().toAbsolutePath();
  }

}