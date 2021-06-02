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

  private Map<String, Object> attributes;

  private String nameAttributeKey;

  private String name;

  private String email;

  private String picture;

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
    log.info("registration ID: " + registrationId);
    switch (registrationId) {
      case "google":
        return ofGoogle(userNameAttributeName, attributes);
      case "Naver":
        return ofNaver("id", attributes);
      case "kakao":
        //        return ofKakao(userNameAttributeName, attributes);
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

  //  private static OAuthAttributes ofKakao(String userNameAttributeName,
  //                                         Map<String, Object> attributes) {
  //    return OAuthAttributes.builder()
  //                          .name((String) attributes.get("name"))
  //                          .email((String) attributes.get("email"))
  //                          .picture((String) attributes.get("picture"))
  //                          .attributes(attributes)
  //                          .nameAttributeKey(userNameAttributeName)
  //                          .build();
  //  }

  private static OAuthAttributes ofNaver(String userNameAttributeName,
                                         Map<String, Object> attributes) {
    return OAuthAttributes.builder()
                          .name((String) attributes.get("name"))
                          .email((String) attributes.get("email"))
                          .picture((String) attributes.get("picture"))
                          .attributes(attributes)
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
