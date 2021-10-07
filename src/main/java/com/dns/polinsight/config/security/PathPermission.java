package com.dns.polinsight.config.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "custom.permission")
public class PathPermission {

  private String[] resources;

  private String[] admin;

  private String[] manager;

  private String[] panel;

  private String[] user;

  private String[] anonymous;

}
