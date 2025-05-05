package com.emelmujiro.secreto.auth.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.emelmujiro.secreto.auth.dto.OAuthUserAttributes;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
		log.info("loadUser");
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = oAuth2UserService.loadUser(request);

		log.info("oAuthUser={}", oAuth2User);

		final String provider = request.getClientRegistration().getRegistrationId();

		Map<String, Object> attributes = oAuth2User.getAttributes();
		OAuthUserAttributes userAttributes = new OAuthUserAttributes(provider, attributes);
		final String email = userAttributes.getEmail();

		boolean isNewUser = false;
		User user = userRepository.findOAuthUserByEmail(provider, email).orElse(null);
		if (user == null) {
			user = userRepository.save(userAttributes.toEntity());
			isNewUser = true;
			log.info("New user created: {}", user.getEmail());
		}

		Map<String, Object> attributeMap = new HashMap<>();
		attributeMap.put("userId", user.getId());
		attributeMap.put("isNewUser", isNewUser);
		attributeMap.put("email", userAttributes.getEmail());
		attributeMap.put("provider", userAttributes.getProvider());

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(String.format("ROLE_%s", "USER"))),
			attributeMap,
			"email"
		);
	}
}
