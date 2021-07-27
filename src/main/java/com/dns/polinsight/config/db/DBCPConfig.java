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

  @Bean
  public DataSource hikariDataSource() {
    HikariConfig config = new HikariConfig();
    config.setPoolName("Test-DBCPPool");
    config.setUsername("java");
    config.setPassword("1234");
    config.setLeakDetectionThreshold(30000);
    config.setMaximumPoolSize(5);
    config.setJdbcUrl(mariadb_URL);
    return new HikariDataSource(config);
  }


}
