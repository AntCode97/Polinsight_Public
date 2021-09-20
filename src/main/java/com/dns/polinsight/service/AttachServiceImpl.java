package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.AttachNotFoundException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.PostRepository;
import com.dns.polinsight.storage.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachServiceImpl implements AttachService {

  private final AttachRepository repository;

  private final PostRepository postRepository;

  private final FileSystemStorageService storageService;

  private final PostService postService;

  @Value("${file.upload.baseLocation}")
  private String baseLocation;

  @Override
  public List<Attach> findAll() {
    return repository.findAll();
  }

  @Override
  public void deleteAttaches(Long postId) {
    repository.findByPostId(postId).forEach(attach -> {
      storageService.delete(attach.getFilePath());
      repository.delete(attach);
    });
  }

  @Override
  public void delete(Attach attach) {
    storageService.delete(attach.getFilePath());
    repository.delete(attach);
  }

  @Override
  public void deleteThumbnail(String thumbnailPath) {
    //경로 문제 때문에 . 추가
    storageService.delete('.'+thumbnailPath);
    log.info("Thumbnail Delete Success!");
  }

  @Override
  public List<File> findFiles(Long postId) {
    //    Post post = postRepository.findById(postId).get();
    //    System.out.println(post.getId() + post.getTitle());
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

  @Override
  public void addAttach(PostDTO postDTO) {
    //System.out.println(postDTO.getFile().getOriginalFilename());
    List<MultipartFile> files = postDTO.getFiles();



    if (files != null) {
      if (!files.isEmpty()) {
        List<Attach> attaches = new ArrayList<>();
        for (MultipartFile file : files) {
          if (!file.isEmpty()) {
            System.out.println(file.getOriginalFilename());
            UUID uuid = UUID.randomUUID();

            Attach attach = Attach.builder()
                                  .fileName(uuid + file.getOriginalFilename())
                                  .fileSize(file.getSize())
                                  .originalName(file.getOriginalFilename())
                                  .filePath(baseLocation + uuid + file.getOriginalFilename())
                                  .post(Post.builder(postDTO).build())
                                  .build();
            attaches.add(attach);
            storageService.store(uuid.toString(), file);

          }
        }
        MultipartFile thumbnailImg = postDTO.getThumbnailImg();
        if(thumbnailImg != null && !thumbnailImg.isEmpty()){
          log.info("썸네일 추가 완료");
          UUID uuid = UUID.randomUUID();

          postDTO.setThumbnail("/"+baseLocation+uuid + thumbnailImg.getOriginalFilename());

          storageService.store(uuid.toString(), thumbnailImg);
        } else {
          log.error("Thumbnail 이미지 파일이 없습니다.");
        }

        repository.saveAll(attaches);
        postDTO.setAttaches(attaches);
        postService.addPost(postDTO);
      }
    } else {
      log.info("File List is null");
    }
  }

}
