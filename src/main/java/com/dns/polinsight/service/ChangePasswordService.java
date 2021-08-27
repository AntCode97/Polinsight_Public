package com.dns.polinsight.service;

import com.dns.polinsight.domain.dto.ChangePwdDto;
import com.dns.polinsight.types.Email;

public interface ChangePasswordService {

  ChangePwdDto findChangePwdDtoByEmail(Email email);

  void saveChangePwdDto(ChangePwdDto changePwdDto);


}
