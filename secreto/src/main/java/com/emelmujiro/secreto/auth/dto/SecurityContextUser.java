package com.emelmujiro.secreto.auth.dto;

import com.emelmujiro.secreto.user.dto.response.UserLoginResponseDto;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.entity.UserRole;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SecurityContextUser(
	Long userId,
	String provider,
	UserRole role,
	String email,
	String username,
	String searchId,
	String profileUrl
) {

	public static SecurityContextUser of(User user) {
		return SecurityContextUser.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.provider(user.getOAuthProvider())
			.username(user.getUsername())
			.searchId(user.getSearchId())
			.profileUrl(user.getProfileUrl())
			.role(user.getRole())
			.build();
	}

	public UserLoginResponseDto toLoginResponse() {
		return UserLoginResponseDto.builder()
			.userId(userId)
			.provider(provider)
			.role(role)
			.email(email)
			.searchId(searchId)
			.profileUrl(profileUrl)
			.build();
	}
}
