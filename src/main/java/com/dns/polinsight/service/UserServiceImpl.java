package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  final String salt = "polinsightPasswordSalt";

  private final UserRepository repository;


  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
  }


  /*
   * Simple CRUD
   * */
  @Override
  public List<User> findAll() {
    return repository.findAll();
  }

  @Override
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  public User findUserById(User user) {
    return repository.findUserById(user.getId()).orElseThrow(UserNotFoundException::new);
  }

  public User find(User user) {
    return repository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public void deleteUser(User user) {
    repository.delete(user);
  }

  @Override
  public User update(User user) {
    return repository.save(user);
  }

  @Override
  public User findUserByEmail(User user) throws UsernameNotFoundException {
    return repository.findUserByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException(user.getEmail()));
  }

  //  /*
  //   * 이메일, 이름만 넘어옴
  //   * */
  //  @Override
  //  @Transactional
  //  public User changeUserPassword(String email, String newPassword) {
  //    // NOTE 2021-06-23 0023 : 해시 발급, 해시 저장
  //    // NOTE 2021-06-23 0023 : Bcrypt 이용 - 넣을 데이터는??
  //    //    passwordEncoder.encode(user.getEmail());
  //    User user = this.findUserByEmail(User.builder().email(email).build());
  //    user.setPassword(passwordEncoder.encode(newPassword));
  //    return repository.save(user);
  //  }

  @Override
  public void sendEmail() {

  }

  @Override
  public String getHash(String email, String username) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();
    digest.update(salt.getBytes(StandardCharsets.UTF_8));
    digest.update(email.getBytes(StandardCharsets.UTF_8));
    digest.update(username.getBytes(StandardCharsets.UTF_8));
    return String.format("%0128x", new BigInteger(1, digest.digest()));
  }

}
