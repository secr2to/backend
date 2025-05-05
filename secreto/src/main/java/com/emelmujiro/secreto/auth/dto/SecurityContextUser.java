package com.emelmujiro.secreto.auth.dto;

import com.emelmujiro.secreto.user.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
public record SecurityContextUser(
	Long userId,
	String provider,
	String role,
	String email,
	String username,
	String nickname,
	String profileUrl
) {

	public static SecurityContextUser of(User user) {
		return SecurityContextUser.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.username(user.getUsername())
			.nickname(user.getNickname())
			.profileUrl(user.getProfileUrl())
			.role("ROLE_USER") /* user 엔티티 권한 필드 추가 */
			.build();
	}

	public LoginResponse toLoginResponse() {
		return LoginResponse.builder()
			.userId(userId)
			.provider(provider)
			.role(role)
			.email(email)
			.nickname(nickname)
			.profileUrl(profileUrl)
			.build();
	}
}
