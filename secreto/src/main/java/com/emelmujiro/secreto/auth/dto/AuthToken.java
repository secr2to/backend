package com.emelmujiro.secreto.auth.dto;

import lombok.Getter;

@Getter
public class AuthToken {

	private final String refreshToken;
	private final String accessToken;

	public AuthToken(String refreshToken, String accessToken) {
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
	}
}
