package com.emelmujiro.secreto.auth.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.auth.service.AuthTokenService;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthTokenServiceImpl implements AuthTokenService {

	@Value("${auth.auth-token-expiration-seconds}")
	private long authTokenExpirationSeconds;

	@Value("${auth.refresh-token-expiration-seconds}")
	private long refreshTokenExpirationSeconds;

	private final ObjectMapper mapper = new ObjectMapper();
	private final RedisTemplate<String, String> authTokenRedisTemplate;
	private final RedisTemplate<String, String> refreshTokenRedisTemplate;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	public AuthTokenServiceImpl(
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
		String value = sanitizeString(authTokenRedisTemplate.opsForValue().get(uuid));
		if (value == null || value.trim().isEmpty()) {
			throw new AuthException(AuthErrorCode.KEY_UUID_INVALID);
		}
		AuthToken token = parseAuthToken(sanitizeString(value));
		authTokenRedisTemplate.delete(uuid);
		return token;
	}

	private AuthToken parseAuthToken(String value) {
		try {
			AuthToken token = mapper.readValue(value, AuthToken.class);
			if (token == null) {
				throw new AuthException(AuthErrorCode.KEY_UUID_INVALID);
			}
			return token;
		} catch (JsonProcessingException e) {
			throw new AuthException(AuthErrorCode.DATA_CONVERSION_FAILED, e);
		}
	}

	public void saveRefreshToken(Long userId, String refreshToken) {
		refreshTokenRedisTemplate.opsForValue().set(
			String.valueOf(userId),
			refreshToken,
			refreshTokenExpirationSeconds,
			TimeUnit.SECONDS
		);
	}

	public void deleteRefreshToken(Long userId) {
		refreshTokenRedisTemplate.delete(String.valueOf(userId));
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

		User user = userRepository.findActiveById(userId)
			.orElseThrow(() -> new AuthException(AuthErrorCode.TOKEN_USER_MISSING));

		return jwtTokenUtil.generateAccessToken(user);
	}

	private String sanitizeString(String value) {
		if (value == null)
			return null;
		return value.strip().replace("\0", "");
	}
}
