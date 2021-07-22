package com.dns.polinsight.service;

import com.dns.polinsight.domain.PointRequest;
import com.dns.polinsight.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService extends UserDetailsService {

  boolean isExistUser(String email);

  List<User> findAll();

  void deleteUserByEmail(String email);

  User save(User user);

  User update(User user);

  User findUserById(User user);

  void deleteUser(User user);


  User findUserByEmail(User user);

  String makeHashForChangePassword(String email, String username) throws NoSuchAlgorithmException;

}
