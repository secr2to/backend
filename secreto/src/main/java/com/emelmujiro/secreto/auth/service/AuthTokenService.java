package com.emelmujiro.secreto.auth.service;

import com.emelmujiro.secreto.auth.dto.AuthToken;

public interface AuthTokenService {

	String saveAuthToken(AuthToken authToken);

	AuthToken getAuthToken(String uuid);

	void saveRefreshToken(Long userId, String refreshToken);

	String reissueAccessToken(String refreshToken);

	void deleteRefreshToken(Long userId);

}
