package com.dns.polinsight.repository;

import com.dns.polinsight.domain.ChangePwdDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangePwdRepository extends JpaRepository<ChangePwdDto, String> {

  Optional<ChangePwdDto> findChangePwdDtoByEmail(String email);

}
