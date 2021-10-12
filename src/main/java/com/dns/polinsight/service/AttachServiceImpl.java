package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.AttachNotFoundException;
import com.dns.polinsight.exception.ImageResizeException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.PostRepository;
import com.dns.polinsight.storage.FileSystemStorageService;
import com.dns.polinsight.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachServiceImpl implements AttachService {

  private final AttachRepository repository;

  private final PostRepository postRepository;

  private final Pattern extPattern = Pattern.compile("\\.(?<ext>png|jpg|jpeg|bmp|gif)$");

  @Value("${file.upload.baseLocation}")
  private String baseLocation;

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
    List<Attach> attaches = repository.findByPostId(postId);
    List<MultipartFile> mFiles = new ArrayList<MultipartFile>();
    for (Attach attach : attaches) {
      File file = new File(attach.getFilePath());
      FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, attach.getOriginalName(), (int) file.length(), file.getParentFile());

      try {
        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);
        // Or faster..
        // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
      } catch (IOException ex) {
        // do something.
      }

      MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

      mFiles.add(multipartFile);
    }
    return mFiles;
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

  //Attach객체를 하나로 만드는 것으로 변경
  @Override
  public Attach addAttach(UUID uuid, MultipartFile file, PostDTO postDTO) {
    Attach attach = Attach.builder()
            .fileName(uuid + file.getOriginalFilename())
            .fileSize(file.getSize())
            .originalName(file.getOriginalFilename())
            .filePath(baseLocation + uuid + file.getOriginalFilename())
            .post(Post.of(postDTO))
            .build();
    repository.save(attach);
    return attach;
  }

}
