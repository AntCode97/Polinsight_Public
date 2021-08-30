package com.dns.polinsight.repository;

import com.dns.polinsight.domain.Panel;
import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.types.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserJdbcTemplate {

  static RowMapper<User> userRowMapper = (rs, rowNum) -> User.builder()
                                                             .id(rs.getLong("id"))
                                                             .point(rs.getLong("point"))
                                                             .name(rs.getString("name"))
                                                             .registeredAt(LocalDate.parse(rs.getString("registered_at")))
                                                             .email(Email.of(rs.getString("email")))
                                                             .role(UserRoleType.valueOf(rs.getString("role")))
                                                             .isEmailReceive(rs.getBoolean("is_email_receive"))
                                                             .isSmsReceive(rs.getBoolean("is_sms_receive"))
                                                             .phone(Phone.of(rs.getString("phone")))
                                                             .recommend(Phone.of(rs.getString("recommend")))
                                                             .password(rs.getString("password"))
                                                             .panel(Panel.builder()
                                                                         .gender(GenderType.valueOf(rs.getString("gender")))
                                                                         .address(Address.of(rs.getString("address")))
                                                                         .birth(LocalDate.parse(rs.getString("birth")))
                                                                         .birthType(rs.getString("birth_type"))
                                                                         .education(rs.getString("education"))
                                                                         .job(rs.getString("job"))
                                                                         .industry(rs.getString("industry"))
                                                                         .marry(rs.getString("marry"))
                                                                         .build())
                                                             .build();

  private final JdbcTemplate jdbcTemplate;

  public Page<UserDto> findAllUser(Pageable pageable) {
    String query = "select * from user where role NOT LIKE 'ADMIN'" +
        " limit " + pageable.getPageSize() +
        " offset " + pageable.getOffset();
    List<User> users = jdbcTemplate.query(query, userRowMapper);
    List<UserDto> dtoList = users.parallelStream().map(UserDto::new).collect(Collectors.toList());
    query = "SELECT COUNT(*) AS total FROM user WHERE role NOT LIKE 'ADMIN'";
    int count = jdbcTemplate.queryForObject(query, int.class);
    return new PageImpl<>(dtoList, pageable, count);
  }

}
