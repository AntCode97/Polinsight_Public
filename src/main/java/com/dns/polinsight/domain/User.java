package com.dns.polinsight.domain;

import com.dns.polinsight.domain.dto.SignupDTO;
import com.dns.polinsight.domain.dto.UserDto;
import com.dns.polinsight.types.Address;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;
import com.dns.polinsight.types.UserRoleType;
import com.dns.polinsight.types.convereter.EmailAttrConverter;
import com.dns.polinsight.types.convereter.PhoneAttrConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
@ToString
@DynamicUpdate
public class User implements UserDetails {

  @JsonBackReference
  @JsonIgnore
  @OneToMany(targetEntity = ParticipateSurvey.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "user")
  private List<ParticipateSurvey> participateSurvey;


  @Builder.Default
  @Enumerated(EnumType.STRING)
  private UserRoleType role = UserRoleType.USER;

  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @Builder.Default
  @ToString.Exclude
  private List<Post> posts = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Convert(converter = EmailAttrConverter.class, attributeName = "email")
  @Column(unique = true)
  private Email email;

  private String password;

  @Setter
  private String name;

  @Setter
  @Convert(converter = PhoneAttrConverter.class, attributeName = "phone")
  private Phone phone;

  @Convert(converter = PhoneAttrConverter.class, attributeName = "recommend")
  private Phone recommend;

  @PositiveOrZero

  @Setter
  @Builder.Default
  private Long point = 0L;

  @Setter
  @Embedded
  private Panel panel;

  /*이메일 수신 동의 여부*/
  @Column(name = "is_email_receive")
  private Boolean isEmailReceive;

  /*문자 수신 동의 여부*/
  @Column(name = "is_sms_receive")
  private Boolean isSmsReceive;


  @Builder.Default
  private LocalDate registeredAt = LocalDate.now();

  public User(SignupDTO dto) {
    this.email = Email.of(dto.getEmail());
    this.password = dto.getPassword();
    this.name = dto.getName();
    this.phone = Phone.of(dto.getPhone());
    this.recommend = dto.getRecommend() != null ? Phone.of(dto.getRecommend()) : null;
    this.role = dto.getIsPanel() ? UserRoleType.PANEL : UserRoleType.USER;
    this.point = 0L;
    this.isEmailReceive = dto.getIsEmailReceive();
    this.isSmsReceive = dto.getIsSmsReceive();
    if (dto.getIsPanel()) {
      this.panel = Panel.builder()
                        .gender(dto.getGender())
                        .education(dto.getEducation())
                        .marry(dto.getMarry())
                        .birth(LocalDate.parse(dto.getBirth()))
                        .birthType(dto.getBirthType())
                        .industry(dto.getIndustry())
                        .job(dto.getJob())
                        .address(Address.of(dto.getAddress()))
                        .favorite(dto.getFavorite())
                        .build();
    } else {
      this.panel = new Panel();
    }
  }

  public User(UserDto dto) {
    this.id = dto.getId();
    this.point = dto.getPoint();
    this.name = dto.getName();
    this.email = Email.of(dto.getEmail());
    this.phone = Phone.of(dto.getPhone());
    this.isSmsReceive = dto.getIsSmsReceive();
    this.isEmailReceive = dto.getIsEmailReceive();
    this.recommend = Phone.of(dto.getRecommend());
    this.participateSurvey = new ArrayList<>();
    this.role = dto.getRole();
    this.registeredAt = dto.getRegisteredAt();
    this.panel = Panel.builder()
                      .gender(dto.getGender())
                      .education(dto.getEducation())
                      .marry(dto.getMarry())
                      .birth(dto.getBirth())
                      .birthType(dto.getBirthType())
                      .industry(dto.getIndustry())
                      .job(dto.getJob())
                      .address(Address.of(dto.getAddress()))
                      .favorite(dto.getFavorite())
                      .build();
  }


  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return this.email.toString();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @PrePersist
  public void setDefaultValue() {
    this.registeredAt = LocalDate.now();
  }

  public void addParticipateSurvey(ParticipateSurvey participateSurvey) {
    if (this.getParticipateSurvey() == null) {
      this.participateSurvey = new ArrayList(Collections.singleton(participateSurvey));
    } else {
      this.participateSurvey.add(participateSurvey);
    }
  }

  public void updatePoint(Long point) {
    this.point += point;
  }

  public void updateRole(UserRoleType role) {
    this.role = role;
  }

}
