package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.types.UserRoleType;
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
@ToString
public class SessionUser implements Serializable {

  private static final long serialVersionUID = -1652804180922488046L;

  @NotNull
  private String name;

  @NotNull
  @Positive
  private Long id;

  @NotNull
  private String email;

  private String picture;

  private UserRoleType role;

  @PositiveOrZero
  private Long point;

  public SessionUser(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.name = user.getName();
    this.point = user.getPoint();
  }

}
