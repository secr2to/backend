package com.emelmujiro.secreto.auth.handler;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<?>> handleCustomException(ApiException e) {
		log.info("auth exception!! code={}", e.getErrorCode());

		Map<String, Object> data = null;
		if (e.getErrorCode() == AuthErrorCode.REFRESH_TOKEN_EXPIRED) {
			data = Map.of("tokenType", "refreshToken");
		} else if (e.getErrorCode() == AuthErrorCode.ACCESS_TOKEN_EXPIRED) {
			data = Map.of("tokenType", "accessToken");
		}
		return ApiResponse.builder().data(data).error(e.getErrorCode());
	}
}
