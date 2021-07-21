package com.dns.polinsight.repository;

import com.dns.polinsight.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserById(Long id);

  boolean existsUserByEmail(String email);

}
