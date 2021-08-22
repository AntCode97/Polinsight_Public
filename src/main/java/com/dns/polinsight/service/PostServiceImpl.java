package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Post;
import com.dns.polinsight.domain.dto.PostDTO;
import com.dns.polinsight.exception.PostNotFoundException;
import com.dns.polinsight.object.PostVO;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.PostRepository;
import com.dns.polinsight.types.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository repository;

  private final AttachRepository attachRepository;

  //@Cacheable
  @Override
  public List<Post> findAll() {
    return repository.findAll();
  }


  @Override
  public Post findOne(Long boardId) {
    return repository.findById(boardId).orElseThrow(PostNotFoundException::new);
  }

  @Override
  public Post find(Post post) {
    return repository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
  }


  @Override
  public Post addPost(PostDTO postDTO) {
    return repository.save(Post.builder(postDTO).build());
  }

  @Override
  public Page<PostVO> findPostsByType(PostType type, Pageable pageable) {
    return repository.findPostsByType(type.name(), pageable);
  }


  @Override
  public void delete(Post post) {
    List<Attach> attaches = attachRepository.findByPostId(post.getId());
    attaches.forEach(attachRepository::delete);
    repository.delete(post);
  }


  public Page<Post> getPostList(Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id"); // <- Sort 추가

    return repository.findAll(pageable);
  }

  @Override
  public Page<Post> searchTitle(String title, PostType postType, Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id"); // <- Sort 추가
    return repository.findByTitle(title, postType, pageable);

  }

  @Override
  public Page<Post> searchContent(String searchcontent, PostType postType, Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id"); // <- Sort 추가
    return repository.findBySearchcontent(searchcontent, postType, pageable);

  }

  @Override
  public Page<Post> searchKeyword(String keyword, Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id"); // <- Sort 추가
    return repository.findBySearchKeyword(keyword, pageable);

  }

  @Override
  public void upViewCnt(Long postId) {
    //post.setViewcnt(post.getViewcnt() +1);
    repository.upViewCnt(postId);
    return;
  }

}
