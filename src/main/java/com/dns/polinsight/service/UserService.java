package com.dns.polinsight.service;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.projection.ExcelUserMapping;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import com.dns.polinsight.types.UserRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {


  List<User> findAll();

  Page<UserDto> findAll(Pageable pageable);


  Page<UserDto> findAllNotInAdmin(Pageable pageable);

  void deleteUserByEmail(Email email);

  User saveOrUpdate(User user);

  Optional<User> findById(long id);

  void deleteUserById(long userId);

  User findUserByEmail(Email email) throws UsernameNotFoundException;

  long countAllUser();

  long countAllUserExcludeAdmin();


  Boolean isExistEmail(Email email);

  Boolean isExistPhone(Phone phone);

  void subUserPoint(long uid, long point);

  User addPointByUserId(long uid, long point);

  void adminUserUpdate(long uid, UserRoleType roleType, long point);


  Optional<User> findUserEmailByNameAndPhone(String name, Phone phone);


  List<ExcelUserMapping> findAllUserToUserDto();

}
