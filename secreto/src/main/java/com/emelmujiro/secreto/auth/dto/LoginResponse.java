package com.emelmujiro.secreto.auth.dto;

import com.emelmujiro.secreto.user.entity.UserRole;

import lombok.Builder;

@Builder
public record LoginResponse(
	Long userId,
	String provider,
	UserRole role,
	String roleName,
	String email,
	String searchId,
	String profileUrl
) {
	public LoginResponse {
		roleName = (role != null) ? role.getName() : null;
	}
}