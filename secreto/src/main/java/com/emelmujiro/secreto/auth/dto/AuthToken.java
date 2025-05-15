package com.emelmujiro.secreto.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthToken {

	private String refreshToken;
	private String accessToken;
	private String tokenType;

	public static AuthToken ofBearer(String refreshToken, String accessToken) {
		return new AuthToken(refreshToken, accessToken, "Bearer");
	}

	public AuthToken(String refreshToken, String accessToken, String tokenType) {
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
}
