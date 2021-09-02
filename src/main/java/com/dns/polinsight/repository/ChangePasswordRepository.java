package com.dns.polinsight.repository;

import com.dns.polinsight.domain.dto.ChangePwdDto;
import com.dns.polinsight.types.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangePasswordRepository extends JpaRepository<ChangePwdDto, String> {

  ChangePwdDto findByEmail(Email email);

}
