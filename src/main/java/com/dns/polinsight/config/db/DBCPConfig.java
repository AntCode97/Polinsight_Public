package com.dns.polinsight.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBCPConfig {

  @Value("${spring.datasource.url}")
  private String mariadb_URL;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.hikari.pool-name}")
  private String poolName;

  @Value("${spring.datasource.hikari.maximum-pool-size}")
  private int maximumPoolSize;

  @Bean
  public DataSource hikariDataSource() {
    HikariConfig config = new HikariConfig();
    config.setPoolName(poolName);
    config.setUsername(username);
    config.setPassword(password);
    config.setLeakDetectionThreshold(30000);
    config.setMaximumPoolSize(maximumPoolSize);
    config.setJdbcUrl(mariadb_URL);
    return new HikariDataSource(config);
  }


}
