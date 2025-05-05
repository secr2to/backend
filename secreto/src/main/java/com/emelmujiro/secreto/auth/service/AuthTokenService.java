package com.emelmujiro.secreto.auth.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthTokenService {

	@Value("${auth.auth-token-expiration-seconds}")
	private long authTokenExpirationSeconds;

	@Value("${auth.refresh-token-expiration-seconds}")
	private long refreshTokenExpirationSeconds;

	private final ObjectMapper mapper = new ObjectMapper();
	private final RedisTemplate<String, String> authTokenRedisTemplate;
	private final RedisTemplate<String, String> refreshTokenRedisTemplate;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	public AuthTokenService(
		@Qualifier("authTokenRedisTemplate") RedisTemplate<String, String> authTokenRedisTemplate,
		@Qualifier("refreshTokenRedisTemplate") RedisTemplate<String, String> refreshTokenRedisTemplate,
		UserRepository userRepository,
		JwtTokenUtil jwtTokenUtil
	) {
		this.authTokenRedisTemplate = authTokenRedisTemplate;
		this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
		this.userRepository = userRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	public String saveAuthToken(AuthToken authToken) {
		UUID uuid = UUID.randomUUID();
		try {
			String value = mapper.writeValueAsString(authToken);
			authTokenRedisTemplate.opsForValue().set(
				uuid.toString(),
				value,
				authTokenExpirationSeconds,
				TimeUnit.SECONDS
			);
		} catch (JsonProcessingException e) {
			throw new AuthException(AuthErrorCode.DATA_CONVERSION_FAILED, e);
		}
		return uuid.toString();
	}

	public AuthToken getAuthToken(String uuid) {
		AuthToken token;
		try {
			String value = sanitizeString(authTokenRedisTemplate.opsForValue().get(uuid));
			token = mapper.readValue(value, AuthToken.class);
		} catch (JsonProcessingException e) {
			throw new AuthException(AuthErrorCode.DATA_CONVERSION_FAILED, e);
		}
		if (token == null) {
			throw new AuthException(AuthErrorCode.KEY_UUID_INVALID);
		}
		return token;
	}

	public void saveRefreshToken(Long userId, String refreshToken) {
		refreshTokenRedisTemplate.opsForValue().set(
			String.valueOf(userId),
			refreshToken,
			refreshTokenExpirationSeconds,
			TimeUnit.SECONDS
		);
	}

	public String reissueAccessToken(String refreshToken) {
		if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
			throw new AuthException(AuthErrorCode.WRONG_TOKEN_TYPE);
		}
		Long userId = jwtTokenUtil.getUserId(refreshToken);
		String storedRefreshToken = sanitizeString(refreshTokenRedisTemplate.opsForValue().get(String.valueOf(userId)));

		if (!refreshToken.equals(storedRefreshToken)) {
			throw new AuthException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
		}

		/**
		 * TODO: User Exception 생성 필요
		 */
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("user not found"));

		return jwtTokenUtil.generateAccessToken(user);
	}

	private String sanitizeString(String value) {
		if (value == null)
			return null;
		return value.strip().replace("\0", "");
	}
}
