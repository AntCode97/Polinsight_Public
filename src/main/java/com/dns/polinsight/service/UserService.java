package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.UserRoleType;
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

  User saveOrUpdate(User user);

  Optional<User> findById(long id);

  void deleteUserById(long userId);

  User findUserByEmail(User user);

  long countAllUser();

  long countAllUserExcludeAdmin();

  Boolean isExistEmail(String email);

  Boolean isExistPhone(String phone);

  void subUserPoint(long uid, long point);

  User addPointByUserId(long uid, long point);

  void adminUserUpdate(long uid, UserRoleType roleType, long point);

  Optional<User> findUserEmailByNameAndPhone(String name, String phone);

}
