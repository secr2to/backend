package com.emelmujiro.secreto.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.service.AuthTokenService;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;
import com.emelmujiro.secreto.auth.util.SecurityContextUtil;
import com.emelmujiro.secreto.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthTokenService authTokenService;
	private final JwtTokenUtil jwtTokenUtil;

	@GetMapping("/redirect")
	public ResponseEntity<?> redirect() {
		return ApiResponse.builder()
			.success();
	}

	@GetMapping("/token")
	public ResponseEntity<?> getToken(@RequestParam("tempId") String tempId) {
		return ApiResponse.builder()
			.data(authTokenService.getAuthToken(tempId))
			.success();
	}

	@GetMapping("/me")
	public ResponseEntity<?> getUserInfo() {
		SecurityContextUser principal = SecurityContextUtil.getPrincipal();
		return ApiResponse.builder()
			.data(principal.toLoginResponse())
			.success();
	}

	@GetMapping("/refresh-access-token")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		String refreshToken = jwtTokenUtil.resolveAuthorization(request);
		final String reissuedAccessToken = authTokenService.reissueAccessToken(refreshToken);
		return ApiResponse.builder()
			.data(Map.of("accessToken", reissuedAccessToken))
			.success();
	}
}
