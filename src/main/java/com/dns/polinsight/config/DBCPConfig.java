package com.dns.polinsight.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBCPConfig {

  @Value ("{spring.datasource.url}")
  private String mariadb_URL;

  @Bean
  public HikariDataSource hikariDataSource(){
    HikariConfig config = new HikariConfig();
    config.setPoolName("Test-DBCPPool");
    config.setUsername("root");
    config.setPassword("root");
    config.setMaximumPoolSize(100);
    config.setJdbcUrl(mariadb_URL);
    return new HikariDataSource(config);
  }

}
