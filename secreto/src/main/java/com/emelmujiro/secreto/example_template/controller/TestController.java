package com.emelmujiro.secreto.example_template.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping
	public ResponseEntity<?> api() {
		log.info("api call");
		HttpHeaders headers = new HttpHeaders();
		headers.put("test", Collections.singletonList("tttt"));

		return ApiResponse.builder()
			.data(new HashMap<>())
			.status(HttpStatus.PARTIAL_CONTENT)
			.headers(headers)
			.message("msg")
			.success();
	}

	@GetMapping("/error")
	public ResponseEntity<?> apiError() {
		log.info("error call");
		throw new ApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/refresh")
	public ResponseEntity<?> refresh() {
		Map<String, String> data = new HashMap<>();
		data.put("tokenType", "refreshToken");

		return ApiResponse.builder()
			.data(data)
			.error(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
	}

	@GetMapping("/refresh-success")
	public ResponseEntity<?> refreshSuccess() {
		Map<String, String> data = new HashMap<>();
		data.put("accessToken", "1234567");

		return ApiResponse.builder()
			.data(data)
			.success();
	}

	@GetMapping("/access")
	public ResponseEntity<?> access() {
		Map<String, String> data = new HashMap<>();
		data.put("tokenType", "accessToken");

		return ApiResponse.builder()
			.data(data)
			.error(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
	}
}

