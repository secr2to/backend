package com.emelmujiro.secreto.example_template.controller;

import java.util.Collections;
import java.util.HashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;
import com.emelmujiro.secreto.global.response.ApiResponse;

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
}

