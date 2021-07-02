package com.dns.polinsight.config.oauth;

import com.dns.polinsight.domain.User;
import com.dns.polinsight.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class OAuthAttributes {

  private final Map<String, Object> attributes;

  private final String nameAttributeKey;

  private final String name;

  private final String email;

  private final String picture;

  @Builder
  public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  public static OAuthAttributes of(String registrationId,
                                   String userNameAttributeName,
                                   Map<String, Object> attributes) {
    switch (registrationId) {
      case "google":
        return ofGoogle(userNameAttributeName, attributes);
      case "naver":
        return ofNaver("id", attributes);
      case "kakao":
        return ofKakao("id", attributes);
    }

    return null;
  }

  private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                          Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");
    return OAuthAttributes.builder()
                          .name((String) attributes.get("name"))
                          .email((String) attributes.get("email"))
                          .picture((String) attributes.get("profile_image"))
                          .attributes(response)
                          .nameAttributeKey(userNameAttributeName)
                          .build();
  }

  private static OAuthAttributes ofKakao(String userNameAttributeName,
                                         Map<String, Object> attributes) {
    Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profiles = (Map<String, Object>) kakao_account.get("profile");
    return OAuthAttributes.builder()
                          .name((String) profiles.get("nickname")) // 카카오는 닉네임만 제공, 실명이 필요하다면 따로 받아야 함
                          .email((String) kakao_account.get("email"))
                          .picture((String) profiles.get("profile_image_url"))
                          .attributes(attributes)
                          .nameAttributeKey(userNameAttributeName)
                          .build();
  }

  private static OAuthAttributes ofNaver(String userNameAttributeName,
                                         Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");
    return OAuthAttributes.builder()
                          .name((String) response.get("name"))
                          .email((String) response.get("email"))
                          .picture((String) response.get("profile_image"))
                          .attributes(response)
                          .nameAttributeKey(userNameAttributeName)
                          .build();
  }


  public User toEntity() {
    return User.builder()
               .name(name)
               .email(email)
               .role(UserRole.USER)
               .build();
  }

}
