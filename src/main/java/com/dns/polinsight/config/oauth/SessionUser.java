package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
public class SessionUser implements Serializable {

  @NotNull
  private String name;

  @NotNull
  @Positive
  private Long id;

  @NotNull
//  @Email(message = "이메일 형식이 다릅니다.")
  private String email;

  private String picture;

  private UserRole role;

  @PositiveOrZero
  private Long point;

  public SessionUser(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.name = user.getName();
    this.point = user.getPoint();
  }

  public SessionUser of(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.name = user.getName();
    this.point = user.getPoint();
    return this;
  }

}
