package com.dns.polinsight.config.oauth;

import com.dns.polinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/*
 * OAuth2를 이용한 소셜 로그인 성공 후의 처리를 위한 클래스
 * 로그인 이후 가져온 정보를 이용해 유저 정보 저장, 수정, 세션 저장등의 기능 지원
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


  private final UserRepository repository;

  private final HttpSession httpSession;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

    OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

    //    User user = saveOrUpdate(attributes);
    //    httpSession.setAttribute("user", new SessionUser(user));
    return null;
    //    return new DefaultOAuth2User(
    //        Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
    //        attributes.getAttributes(),
    //        attributes.getNameAttributeKey()
    //    );
  }

  //  private User saveOrUpdate(OAuthAttributes attributes) {
  //    User user = repository.findUserByEmail(attributes.getEmail())
  //                          .map(entity -> entity.update(attributes.getName())).orElse(attributes.toEntity());
  //    return repository.save(user);
  //
  //  }

}
