package com.emelmujiro.secreto.user.dto.response;

import com.emelmujiro.secreto.user.entity.UserRole;

import lombok.Builder;

@Builder
public record UserLoginResponseDto(
	Long userId,
	String provider,
	UserRole role,
	String roleName,
	String email,
	String searchId,
	String profileUrl
) {
	public UserLoginResponseDto {
		roleName = (role != null) ? role.getName() : null;
	}
}