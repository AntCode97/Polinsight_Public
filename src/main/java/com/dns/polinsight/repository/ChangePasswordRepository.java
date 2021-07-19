package com.dns.polinsight.repository;

import com.dns.polinsight.domain.dto.ChangePwdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangePasswordRepository extends JpaRepository<ChangePwdDto, String> {
}
