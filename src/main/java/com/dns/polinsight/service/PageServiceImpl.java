package com.dns.polinsight.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

  @Override
  //  @Cacheable
  public String getTerms() throws IOException {
    ClassPathResource resource = new ClassPathResource("static/assets/Terms.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
    StringBuilder sb = new StringBuilder();
    while (br.ready()) {
      sb.append(br.readLine());
    }
    return sb.toString();
  }

}
