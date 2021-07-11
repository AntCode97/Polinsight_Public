package com.dns.polinsight.service;

import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.repository.AddtionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdditionalServiceImpl implements AdditionalService {

  private final AddtionalRepository repository;

  @Override
  public Additional save(Additional additional) {
    return repository.save(additional);
  }

  @Override
  public Additional find(User user) throws Exception {
    return repository.findAdditionalByUser(user).orElseThrow(Exception::new);
  }

}
