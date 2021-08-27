package com.dns.polinsight.service;

import com.dns.polinsight.config.security.CustomAuthManager;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.repository.UserJdbcTemplate;
import com.dns.polinsight.repository.UserRepository;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import com.dns.polinsight.types.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  private final UserJdbcTemplate userJdbcTemplate;

  @Override
  public List<User> findAll() {
    return repository.findAll();
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    String[] emails = username.split("@");
    return repository.findUserByEmail(Email.builder().account(emails[0]).domain(emails[1]).build()).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
  }

  @Override
  public Page<UserDto> findAll(Pageable pageable) {
    Page<User> page = repository.findAll(pageable);
    List<UserDto> dtoList = page.getContent().parallelStream().map(UserDto::new).collect(Collectors.toList());
    return new PageImpl<>(dtoList, pageable, page.getTotalElements());
  }

  @Override
  public Page<UserDto> findAllNotInAdmin(Pageable pageable) {
    Page<User> page = repository.findAllByRoleIsNotLike(pageable, UserRoleType.ADMIN);
    List<UserDto> dtoList = page.getContent().parallelStream().map(UserDto::new).collect(Collectors.toList());
    return new PageImpl<>(dtoList, pageable, page.getTotalElements());
  }

  @Override
  public void deleteUserByEmail(Email email) {
    repository.deleteUserByEmail(email);
  }

  @Override
  public User saveOrUpdate(User user) {
    user = repository.saveAndFlush(user);
    log.warn(this.getClass().getSimpleName() + " ::: " + user.toString());
    return user;
  }

  @Override
  public Optional<User> findById(long id) {
    return repository.findById(id);
  }

  @Override
  public void deleteUserById(long userId) {
    repository.deleteById(userId);
  }

  @Override
  public User findUserByEmail(Email email) throws UsernameNotFoundException {
    return repository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email.toString()));
  }

  @Override
  public long countAllUser() {
    return repository.count();
  }

  @Override
  public long countAllUserExcludeAdmin() {
    return repository.userCountExcludeAdmin();
  }

  @Override
  public Boolean isExistEmail(Email email) {
    return repository.existsUserByEmail(email);
  }

  @Override
  public Boolean isExistPhone(Phone phone) {
    return repository.existsUserByPhone(phone);
  }

  @Override
  public void subUserPoint(long uid, long point) {
    repository.subtractUserPointByUid(uid, point);
  }

  @Override
  public User addPointByUserId(long uid, long point) {
    return repository.userAddPointById(uid, point);
  }

  @Override
  public void adminUserUpdate(long uid, UserRoleType roleType, long point) {
    repository.adminUpdateUser(uid, roleType.name(), point);
  }

  @Override
  public Optional<User> findUserEmailByNameAndPhone(String name, Phone phone) {
    return repository.findUserByNameAndPhone(name, phone);
  }

  @Override
  public Page<UserDto> testFindAllUser(Pageable pageable) {
    return userJdbcTemplate.findAllUser(pageable);
  }


}
