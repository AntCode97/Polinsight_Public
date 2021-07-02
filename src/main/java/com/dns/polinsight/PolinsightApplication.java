package com.dns.polinsight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@EnableCaching
@EnableRedisHttpSession
@SpringBootApplication
public class PolinsightApplication {

  @Autowired
  private static ApplicationContext context;

  public static void main(String[] args) {
    SpringApplication.run(PolinsightApplication.class, args);
  }

}
