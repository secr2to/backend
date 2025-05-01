package com.emelmujiro.secreto.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.service.NaverOAuthService;
import com.emelmujiro.secreto.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final NaverOAuthService naverOAuthService;

	@GetMapping("/naver-login")
	public ResponseEntity<?> naverLogin() {
		return ApiResponse.builder()
			.data(naverOAuthService.getNaverLoginUrl())
			.success();
	}

	@GetMapping("/naver-login/redirect")
	public ResponseEntity<?> naverLoginRedirect(HttpServletRequest request) {
		final String code = request.getParameter("code");
		final String state = request.getParameter("state");

		return ApiResponse.builder()
			.data(naverOAuthService.naverLogin(code, state))
			.success();
	}
}

