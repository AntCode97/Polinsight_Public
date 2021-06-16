package com.dns.polinsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PolinsightApplication {

  public static void main(String[] args) {
    SpringApplication.run(PolinsightApplication.class, args);
  }

}
