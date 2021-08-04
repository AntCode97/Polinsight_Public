package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


  private final UserRepository repository;


  @Override
  public boolean isExistUser(String email) {
    return repository.existsUserByEmail(email);
  }

  @Override
  public List<User> findAll() {
    return repository.findAll();
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return repository.findAll(pageable);
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
  public Optional<User> findById(long id) {
    return repository.findById(id);
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
  public long countAllUser() {
    return repository.count();
  }

  @Override
  public Boolean isExistEmail(String email) {
    return repository.existsUserByEmail(email);
  }

  @Override
  public Boolean isExistPhone(String phone) {
    return repository.existsUserByEmail(phone);
  }

  @Override
  public void subUserPoint(long uid, long point) {
    repository.subtractUserPointByUid(uid, point);
  }

}
