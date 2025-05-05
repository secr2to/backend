package com.emelmujiro.secreto.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
	Long userId,
	String provider,
	String role,
	String email,
	String nickname,
	String profileUrl
) {
}
