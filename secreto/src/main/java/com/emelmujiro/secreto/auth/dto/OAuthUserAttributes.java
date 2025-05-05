package com.emelmujiro.secreto.auth.dto;

import java.util.HashMap;
import java.util.Map;

import com.emelmujiro.secreto.user.entity.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OAuthUserAttributes {

	private String provider;
	private String email;
	private String username;
	private String nickname;
	private String profileUrl;

	public OAuthUserAttributes(String provider, Map<String, Object> attributes) {
		this.provider = provider;
		switch (provider) {
			case "naver" -> mapUserAttributesOfNaver(attributes);
			case "kakao" -> mapUserAttributeOfKakao(attributes);
			case "google" -> mapUserAttributeOfGoogle(attributes);
			default -> throw new IllegalStateException("Unexpected value: " + provider);
		}
	}

	private void mapUserAttributesOfNaver(Map<String, Object> attributes) {
		Map<String, Object> userInfo = getUserInfo(provider, attributes);
		this.email = getString(userInfo, "email");
		this.username = getString(userInfo, "id");
		this.nickname = getString(userInfo, "nickname");
		this.profileUrl = getString(userInfo, "profile_image");
	}

	@SuppressWarnings("unchecked")
	private void mapUserAttributeOfKakao(Map<String, Object> attributes) {
		Map<String, Object> userInfo = getUserInfo(provider, attributes);
		Map<String, Object> profileInfo = (Map<String, Object>) userInfo.get("profile");
		this.email = getString(userInfo, "email");
		this.username = String.valueOf(getLong(attributes, "id"));
		this.nickname = getString(profileInfo, "nickname");
		this.profileUrl = getString(profileInfo, "profile_image_url");
	}

	private void mapUserAttributeOfGoogle(Map<String, Object> attributes) {
		Map<String, Object> userInfo = getUserInfo(provider, attributes);
	}

	public User toEntity() {
		return User.oauthUserBuilder()
			.oAuthProvider(provider)
			.email(email)
			.username(username)
			.nickname(nickname)
			.profileUrl(profileUrl)
			.build();
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> getUserInfo(String provider, Map<String, Object> attributes) {
		return switch (provider) {
			case "naver" -> (Map<String, Object>) attributes.get("response");
			case "kakao" -> (Map<String, Object>) attributes.get("kakao_account");
			case "google" -> new HashMap<>();
			default -> throw new IllegalStateException("Unexpected value: " + provider);
		};
	}

	private static String getString(Map<String, Object> attributes, String key) {
		Object value = attributes.get(key);
		return value != null ? (String) attributes.get(key) : null;
	}

	private static Long getLong(Map<String, Object> attributes, String key) {
		Object value = attributes.get(key);
		return value != null ? (Long) attributes.get(key) : null;
	}
}
