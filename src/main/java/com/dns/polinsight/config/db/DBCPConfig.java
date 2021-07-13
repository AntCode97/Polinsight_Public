package com.dns.polinsight.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DBCPConfig {

  @Value("${spring.datasource.url}")
  private String mariadb_URL;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public DataSource hikariDataSource() {
    HikariConfig config = new HikariConfig();
    config.setPoolName("Test-DBCPPool");
    config.setUsername(username);
    config.setPassword(password);
    config.setLeakDetectionThreshold(30000);
    config.setMaximumPoolSize(100);
    config.setJdbcUrl(mariadb_URL);
    log.info("dburl: " + mariadb_URL + ", user: " + username);
    return new HikariDataSource(config);
  }

}
