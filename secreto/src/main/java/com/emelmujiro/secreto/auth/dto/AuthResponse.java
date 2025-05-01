package com.emelmujiro.secreto.auth.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

	private final String accessToken;
	private final String refreshToken;
	private final String tokenType;
	private final String expiresIn;

	public static AuthResponse ofNaver(JsonNode response) {
		return AuthResponse.builder()
			.accessToken(response.path("access_token").asText())
			.refreshToken(response.path("refresh_token").asText())
			.tokenType(response.path("token_type").asText())
			.expiresIn(response.path("expires_in").asText())
			.build();
	}
}
