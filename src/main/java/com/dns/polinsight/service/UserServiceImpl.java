package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
  public boolean isExistUser(String email) {
    return repository.existsUserByEmail(email);
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
  }

  @Override
  public List<User> findAll() {
    return repository.findAll();
  }

  @Override
  public void deleteUserByEmail(String email) {
    repository.deleteUserByEmail(email);
  }

  @Override
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  public User update(User user) {
    return repository.saveAndFlush(user);
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
  public User findUserByEmail(User user) throws UsernameNotFoundException {
    return repository.findUserByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException(user.getEmail()));
  }

  @Override
  public String makeHashForChangePassword(String email, String username) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    digest.reset();
    digest.update(salt.getBytes(StandardCharsets.UTF_8));
    digest.update(email.getBytes(StandardCharsets.UTF_8));
    digest.update(username.getBytes(StandardCharsets.UTF_8));
    StringBuilder sb = new StringBuilder();
    for (byte b : digest.digest())
      sb.append(String.format("%02x", b));
    return sb.toString();
  }

}
