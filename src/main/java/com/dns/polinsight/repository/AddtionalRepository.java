package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddtionalRepository extends JpaRepository<Additional, Long> {

  Optional<Additional> findAdditionalByUser(User user);

}
