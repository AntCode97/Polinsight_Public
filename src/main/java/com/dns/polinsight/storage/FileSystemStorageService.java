package com.dns.polinsight.storage;

import com.dns.polinsight.exception.ImageResizeException;
import com.dns.polinsight.utils.ImageUtil;
import com.dns.polinsight.utils.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
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
import java.util.stream.Stream;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  private final TypeCheckUtil typeCheckUtil;

  private final ImageUtil imageUtil;

  @Autowired
  public FileSystemStorageService(StorageProperties properties, TypeCheckUtil typeCheckUtil, ImageUtil imageUtil) {
    this.rootLocation = Paths.get(properties.getLocation());
    this.typeCheckUtil = typeCheckUtil;
    this.imageUtil = imageUtil;
  }

  @Override
  public String store(String uuid, MultipartFile file) throws IOException {
    String fileName = null;
    if (file.isEmpty()) {
      throw new StorageException("Failed to store empty file.");
    }
    fileName = uuid + file.getOriginalFilename();
    Path destinationFile = getPathByType(file, uuid);

    // TODO: 2021-10-13 : 경로 체크 구문이 필요할 듯

    // NOTE 2021-10-13 : 빌드시에 디렉토리를 생성하게 해놓아서 문제는 없어보인다. --> 아닌 것 같다
    //    if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
    //      throw new StorageException("Cannot store file outside current directory.");
    //    }

    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }

    return fileName;
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
      File file = new File(Paths.get(String.valueOf(rootLocation), filepath).toString());
      file.delete();
      log.info(file.getName() + " has deleted");
    } catch (SecurityException se) {
      throw new SecurityException(se.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  @PostConstruct
  public void init() {
    try {
      if (!Files.exists(rootLocation))
        Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }


  private Path getPathByType(MultipartFile file, String uuid) {
    if (typeCheckUtil.isImageFile(file.getOriginalFilename()))
      return rootLocation.resolve(Paths.get("image/" + uuid + file.getOriginalFilename())).normalize().toAbsolutePath();
    else
      return rootLocation.resolve(Paths.get("files/" + uuid + file.getOriginalFilename())).normalize().toAbsolutePath();
  }

}