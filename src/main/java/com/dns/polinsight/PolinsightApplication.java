package com.dns.polinsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableCaching
@EnableRedisHttpSession
@SpringBootApplication
public class PolinsightApplication {

  public static void main(String[] args) {
    SpringApplication.run(PolinsightApplication.class, args);
  }

}
