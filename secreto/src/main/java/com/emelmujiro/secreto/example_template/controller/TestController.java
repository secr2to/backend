package com.emelmujiro.secreto.example_template.controller;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping
	public ResponseEntity<?> api(@LoginUser Long userId) {

		log.info("userId={}", userId);
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

	@PostMapping("/{one}/{two}/{three}/{four}/{five}/{six}")
	public ResponseEntity<?> customRequestBody(@RequestBody TestDto testDto,
		@LoginUser Long userId) {
		log.info("userId={}", userId);
		log.info("one={}", testDto.one);
		log.info("two={}", testDto.twotwo);
		log.info("three={}", testDto.three);
		log.info("four={}", testDto.four);
		log.info("five={}", testDto.fivefive);
		log.info("six={}", testDto.six);
		return ApiResponse.builder()
			.success();
	}

	@GetMapping("/{one}")
	public ResponseEntity<?> ModelAttributeTest(@ModelAttribute TestDto2 testDto) {
		log.info("userId={}", testDto.userId);
		log.info("one={}", testDto.one);
		log.info("two={}", testDto.two);
		return ApiResponse.builder()
			.success();
	}

	@Data
	public static class TestDto2 {

		@LoginUser
		private Long userId;

		private Integer one;
		private Double two;
	}

	public static class TestDto {

		@LoginUser
		private Long userId;

		@InjectPathVariable(name = "one")
		private int one;

		@InjectPathVariable(name = "two")
		private Double twotwo;

		@InjectPathVariable
		private Long three;

		@InjectPathVariable
		private long four;

		@InjectPathVariable(name = "five")
		private String fivefive;

		@InjectPathVariable
		private float six;
	}
}

