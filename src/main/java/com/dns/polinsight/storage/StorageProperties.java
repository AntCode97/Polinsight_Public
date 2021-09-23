package com.dns.polinsight.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
public class StorageProperties {

  /**
   * s Folder location for storing files
   */
  @Value("${file.upload.baseLocation}")
  private String location;
//private String location = "static/upload-dir";

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

}