package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.exception.UserNotFoundException;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
  }

  /*
   * Simple CRUD
   * */
  @Override
  @Cacheable
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


  @Cacheable
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

  @Override
  public User changePwd(User user) {
    return null;
  }


}
