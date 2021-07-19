package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService extends UserDetailsService {
  //  Simple User CRUD

  List<User> findAll();

  User save(User user);

  User findUserById(User user);

  void deleteUser(User user);

  User update(User user);

  User findUserByEmail(User user);


  //  /*
  //   * 이메일, 이름만 넘어옴
  //   * */
  //  @Transactional
  //  User changeUserPassword(String email, String newPassword);


  String makeHashForChangePassword(String email, String username) throws NoSuchAlgorithmException;

}
