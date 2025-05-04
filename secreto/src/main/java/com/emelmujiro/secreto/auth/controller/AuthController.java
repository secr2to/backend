package com.emelmujiro.secreto.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	@GetMapping("/redirect")
	public ResponseEntity<?> redirect(
		@RequestParam String accessToken,
		@RequestParam String refreshToken
	) {
		/**
		 * 임시 로직
		 */

		log.info("accessToken={}", accessToken);
		log.info("refreshToken={}", refreshToken);

		return ApiResponse.builder()
			.success();
	}
}
