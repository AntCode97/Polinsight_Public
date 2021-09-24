package com.dns.polinsight.repository;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.projection.ExcelUserMapping;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import com.dns.polinsight.types.UserRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(Email email);

  boolean existsUserByEmail(Email email);

  boolean existsUserByPhone(Phone phone);

  @Query(nativeQuery = true, value = "SELECT * FROM user WHERE email LIKE %?1% OR role LIKE %?1% OR phone LIKE %?1% OR name LIKE %?1%")
  Page<User> findUsersByRegex(String regex, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM user WHERE email LIKE %?1% OR role LIKE %?1% OR phone LIKE %?1% OR name LIKE %?1% AND role NOT LIKE 'ADMIN'")
  long countUsersByRegex(String regex);

  void deleteUserByEmail(Email email);

  void deleteById(long id);

  @Query(nativeQuery = true, value = "UPDATE user SET point = point - ?2 WHERE id = ?1")
  void subtractUserPointByUid(long uid, long point);

  @Query(nativeQuery = true, value = "UPDATE user SET point = point + ?2 WHERE id = ?1")
  User userAddPointById(long id, long point);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM user WHERE role NOT LIKE 'ADMIN'")
  long userCountExcludeAdmin();

  @Query(nativeQuery = true, value = "UPDATE user SET role = ?2, point= ?3 WHERE id = ?1")
  Optional<User> adminUpdateUser(long id, String role, long point);

  Optional<User> findUserByNameAndPhone(String name, Phone phone);

  Page<User> findAllByRoleIsNotLike(Pageable pageable, UserRoleType role);

  @Query("select u from User u")
  List<ExcelUserMapping> findAllUserByUserDto();

}
