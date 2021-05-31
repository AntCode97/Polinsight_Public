package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServlce extends UserDetailsService {
  //  Simple User CRUD

  List<User> findAll();

  User save(User user);

  User find(User user);

  void deleteUser(User user);

  User update(User user);

}
