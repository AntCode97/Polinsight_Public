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
    config.setUsername("root");
    config.setPassword("159753");
    config.setLeakDetectionThreshold(30000);
    config.setMaximumPoolSize(200);
    config.setJdbcUrl(mariadb_URL);
    HikariDataSource dataSource = new HikariDataSource(config);
    System.out.println(dataSource.toString());
    return dataSource;
  }


}
