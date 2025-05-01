package com.emelmujiro.secreto.auth.service;

import static java.util.Collections.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.emelmujiro.secreto.auth.dto.AuthResponse;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuthService {

	private static final String NAVER_LOGIN_BASE_URL = "https://nid.naver.com/oauth2.0";

	@Value("${server.url}")
	private String baseUrl;

	@Value("${login.naver.client-id}")
	private String naverLoginClientId;

	@Value("${login.naver.client-secret}")
	private String naverLoginClientSecret;

	private final WebClient webClient;

	public Map<String, String> getNaverLoginUrl() {
		final String redirectUri = baseUrl + "/auth/naver-login/redirect";
		final String authorizationUrl = NAVER_LOGIN_BASE_URL
			+ "/authorize"
			+ "?response_type=code"
			+ "&client_id=" + naverLoginClientId
			+ "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
			+ "&state=" + UUID.randomUUID();

		return singletonMap("authorizationUrl", authorizationUrl);
	}

	public AuthResponse naverLogin(String code, String state) {
		final String tokenIssuanceEndpoint = NAVER_LOGIN_BASE_URL
			+ "/token"
			+ "?grant_type=authorization_code"
			+ "&client_id=" + naverLoginClientId
			+ "&client_secret=" + naverLoginClientSecret
			+ "&code=" + code
			+ "&state=" + state;

		return webClient.get()
			.uri(tokenIssuanceEndpoint)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.map(AuthResponse::ofNaver)
			.block();
	}
}
