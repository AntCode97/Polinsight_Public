package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.AttachNotFoundException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.utils.TypeCheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachServiceImpl implements AttachService {

  private final AttachRepository repository;

  private final TypeCheckUtil typeCheckUtil;


  @Value("${file.upload.baseLocation}")
  private String baseLocation;

  @Value("${file.upload.image}")
  private String imageLocation;

  @Value("${file.upload.file}")
  private String fileLocation;

  @Override
  public List<Attach> findAll() {
    return repository.findAll();
  }

  @Override
  public List<Attach> findByPostId(Long postId) {
    return repository.findByPostId(postId);
  }

  @Override
  public void deleteAttaches(Long postId) {
    repository.findByPostId(postId).forEach(attach -> {
      repository.delete(attach);
    });
  }


  @Override
  public void delete(Attach attach) {
    repository.delete(attach);
  }


  @Override
  public List<File> findFiles(Long postId) {
    return repository.findByPostId(postId).stream().map(attach -> new File(attach.getFilePath())).collect(Collectors.toList());
  }

  @Override
  public List<MultipartFile> findMultipartFiles(Long postId) throws IOException {
    return repository.findByPostId(postId).stream()
                     .map(attach -> {
                       try {
                         File file = new File(attach.getFilePath());
                         FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, attach.getOriginalName(), (int) file.length(), file.getParentFile());
                         IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                         return new CommonsMultipartFile(fileItem);
                       } catch (IOException e) {
                         log.error(e.getMessage());
                       }
                       return null;
                     }).collect(Collectors.toList());
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
  public List<Attach> findByname(String fileName) {
    return repository.findByFileName(fileName);
  }

  @Override
  public Attach addAttach(UUID uuid, MultipartFile file, PostDTO postDTO) {
    String fileName = uuid + file.getOriginalFilename();
    return repository.save(Attach.builder()
                                 .fileName((typeCheckUtil.isImageFile(file.getOriginalFilename()) ? "images/" : "files/") + fileName)
                                 .fileSize(file.getSize())
                                 .originalName(file.getOriginalFilename())
                                 .filePath((typeCheckUtil.isImageFile(file.getOriginalFilename()) ? imageLocation : fileLocation) + fileName)
                                 .post(Post.of(postDTO))
                                 .build());
  }

}
