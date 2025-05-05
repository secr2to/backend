package com.emelmujiro.secreto.auth.dto;

import com.emelmujiro.secreto.user.entity.UserRole;

import lombok.Builder;

@Builder
public record LoginResponse(
	Long userId,
	String provider,
	UserRole role,
	String email,
	String nickname,
	String profileUrl
) {
}
