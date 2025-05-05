package com.emelmujiro.secreto.auth.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthTokenService {

	@Value("${login.auth-token-expiration-seconds}")
	private long authTokenExpirationSeconds;

	private final ObjectMapper mapper = new ObjectMapper();
	private final RedisTemplate<String, String> redisTemplate;

	public AuthTokenService(@Qualifier("authTokenRedisTemplate") RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String save(AuthToken authToken) {
		UUID uuid = UUID.randomUUID();
		try {
			String value = mapper.writeValueAsString(authToken);
			redisTemplate.opsForValue().set(uuid.toString(), value, authTokenExpirationSeconds);
		} catch (JsonProcessingException e) {
			throw new AuthException(AuthErrorCode.DATA_CONVERSION_FAILED, e);
		}
		return uuid.toString();
	}

	public AuthToken get(String uuid) {
		AuthToken token;
		try {
			String value = sanitizeString(redisTemplate.opsForValue().get(uuid));
			token = mapper.readValue(value, AuthToken.class);
		} catch (JsonProcessingException e) {
			throw new AuthException(AuthErrorCode.DATA_CONVERSION_FAILED, e);
		}
		if (token == null) {
			throw new AuthException(AuthErrorCode.KEY_UUID_INVALID);
		}
		return token;
	}

	private String sanitizeString(String value) {
		if (value == null)
			return null;
		return value.strip().replace("\0", "");
	}
}
