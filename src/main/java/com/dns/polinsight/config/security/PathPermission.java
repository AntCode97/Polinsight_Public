package com.dns.polinsight.config.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "custom.permission")
public class PathPermission {

  private List<String> resources;

  private List<String> admin;

  private List<String> template;

}
