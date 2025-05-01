package com.emelmujiro.secreto.auth.service;

import static java.util.Collections.*;
import static org.springframework.http.HttpHeaders.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.emelmujiro.secreto.auth.dto.AuthResponse;
import com.emelmujiro.secreto.auth.enums.OAuthProvider;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NaverOAuthService {

	private static final String NAVER_OAUTH_BASE_URL = "https://nid.naver.com/oauth2.0";
	private static final String NAVER_OPENAPI_BASE_URL = "https://openapi.naver.com";

	@Value("${server.url}")
	private String baseUrl;

	@Value("${login.naver.client-id}")
	private String naverLoginClientId;

	@Value("${login.naver.client-secret}")
	private String naverLoginClientSecret;

	private final WebClient webClient;

	private final UserRepository userRepository;

	public Map<String, String> getNaverLoginUrl() {
		final String redirectUri = baseUrl + "/auth/naver-login/redirect";
		final String authorizationUrl = NAVER_OAUTH_BASE_URL
			+ "/authorize"
			+ "?response_type=code"
			+ "&client_id=" + naverLoginClientId
			+ "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
			+ "&state=" + UUID.randomUUID();

		return singletonMap("authorizationUrl", authorizationUrl);
	}

	@Transactional
	public AuthResponse naverLogin(String code, String state) {
		final String tokenIssuanceEndpoint = NAVER_OAUTH_BASE_URL
			+ "/token"
			+ "?grant_type=authorization_code"
			+ "&client_id=" + naverLoginClientId
			+ "&client_secret=" + naverLoginClientSecret
			+ "&code=" + code
			+ "&state=" + state;

		JsonNode naverAuthResponse = webClient.get()
			.uri(tokenIssuanceEndpoint)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		if (naverAuthResponse == null || naverAuthResponse.has("error")) {
			throw new AuthException(AuthErrorCode.SESSION_INVALID);
		}

		AuthResponse response = AuthResponse.ofNaver(naverAuthResponse);
		Long userId = saveUser(response.getAccessToken());
		return response;
	}

	/**
	 * Spring Security 추가 후 변경 예정
	 */
	@Transactional
	public Long saveUser(String authorization) {
		JsonNode userProfileResponse = webClient.get()
			.uri(NAVER_OPENAPI_BASE_URL + "/v1/nid/me")
			.header(AUTHORIZATION, "Bearer " + authorization)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		if (userProfileResponse == null || !"success".equals(userProfileResponse.path("message").asText())) {
			throw new AuthException(AuthErrorCode.SESSION_INVALID);
		}

		JsonNode userProfileInfo = userProfileResponse.path("response");
		final String username = userProfileInfo.path("id").asText();

		Optional<User> optional = userRepository.findByUsername(username);
		if (optional.isPresent()) {
			return optional.get().getId();
		}

		final String email = userProfileInfo.path("email").asText();
		final String nickname = userProfileInfo.path("nickname").asText();
		final String profileUrl = userProfileInfo.path("profile_image").asText();

		User user = User.oauthUserBuilder()
			.oAuthProvider(OAuthProvider.NAVER)
			.username(username)
			.email(email)
			.nickname(nickname)
			.profileUrl(profileUrl)
			.build();

		User savedUser = userRepository.save(user);
		log.info("유저 생성! userId={}", savedUser.getId());
		return savedUser.getId();
	}
}
