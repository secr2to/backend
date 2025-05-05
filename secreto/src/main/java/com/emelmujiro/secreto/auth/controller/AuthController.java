package com.emelmujiro.secreto.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.service.AuthTokenService;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthTokenService authTokenService;

	@GetMapping("/redirect")
	public ResponseEntity<?> redirect() {
		return ApiResponse.builder()
			.success();
	}

	@GetMapping("/token")
	public ResponseEntity<?> getToken(@RequestParam String tempId) {
		return ApiResponse.builder()
			.data(authTokenService.getAuthToken(tempId))
			.success();
	}

	@GetMapping("/me")
	public ResponseEntity<?> getUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityContextUser principal = (SecurityContextUser) authentication.getPrincipal();

		return ApiResponse.builder()
			.data(principal.toLoginResponse())
			.success();
	}
}
