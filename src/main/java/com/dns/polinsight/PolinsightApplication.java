package com.dns.polinsight;

import com.dns.polinsight.domain.Board;
import com.dns.polinsight.repository.BoardRepository;
import com.dns.polinsight.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@EnableCaching
@EnableRedisHttpSession
@SpringBootApplication
public class PolinsightApplication {

  public static void main(String[] args) {
    SpringApplication.run(PolinsightApplication.class, args);
  }

  @Bean
  CommandLineRunner init(StorageService storageService) {
    return (args) -> {
      storageService.deleteAll();
      storageService.init();
    };
  }



  //게시판 쿼리 테스트
//  @Bean
//  public CommandLineRunner initData(BoardRepository boardRepository) {
//    return args ->
//            IntStream.rangeClosed(1, 300).forEach(i -> {
//
//
//              Board board = Board.builder()
//                      .title("test" + i)
//                      .viewcontent("test"+i).searchcontent("test"+i).registeredAt(LocalDateTime.now())
//                      .build();
//
//              boardRepository.save(board);
//            });
//  }

}
