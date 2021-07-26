package com.dns.polinsight.service;

import com.dns.polinsight.domain.dto.ChangePwdDto;
import com.dns.polinsight.repository.ChangePasswordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {

  private final ChangePasswordRepository repository;

  @Override
  public ChangePwdDto findChangePwdDtoByEmail(String email) {
    return repository.getById(email);
  }

  @Override
  public void saveChangePwdDto(ChangePwdDto changePwdDto) {
    repository.save(changePwdDto);
  }

}
