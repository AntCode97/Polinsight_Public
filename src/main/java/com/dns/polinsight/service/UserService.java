package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

  boolean isExistUser(String email);

  List<User> findAll();

  Page<User> findAll(Pageable pageable);

  void deleteUserByEmail(String email);

  User save(User user);

  User update(User user);


  Optional<User> findById(long id);

  void deleteUser(User user);

  User findUserByEmail(User user);

  long coutAllUser();

}
