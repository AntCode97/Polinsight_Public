package com.dns.polinsight.service;

import com.dns.polinsight.domain.dto.ChangePwdDto;

public interface ChangePasswordService {

  ChangePwdDto findChangePwdDtoByEmail(String email);

  void saveChangePwdDto(ChangePwdDto changePwdDto);


}
