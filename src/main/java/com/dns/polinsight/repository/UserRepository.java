package com.dns.polinsight.repository;

import com.dns.polinsight.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserById(Long id);

  boolean existsUserByEmail(String email);

  boolean existsUserByPhone(String phone);

  @Query(nativeQuery = true, value = "SELECT * FROM user WHERE email LIKE %?1% OR role LIKE %?1% OR phone LIKE %?1% OR name LIKE %?1%")
  List<User> findUsersByRegex(String regex, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM user WHERE email LIKE %?1% OR role LIKE %?1% OR phone LIKE %?1% OR name LIKE %?1% AND role NOT LIKE 'ADMIN'")
  long countUsersByRegex(String regex);

  void deleteUserByEmail(String email);

  @Query(nativeQuery = true, value = "UPDATE user SET point = point - ?2 WHERE id = ?1")
  void subtractUserPointByUid(long uid, long point);

  @Query(nativeQuery = true, value = "UPDATE user SET point = point + ?2 WHERE id = ?1")
  User userAddPointById(long id, long point);

  @Query(nativeQuery = true, value = "SELECT COUNT(id) FROM user WHERE role NOT LIKE 'ADMIN'")
  long userCountExcludeAdmin();

  @Query(nativeQuery = true, value = "UPDATE user SET role = ?2, point= ?3 WHERE id = ?1")
  Optional<User> adminUpdateUser(long id, String role, long point);

}
