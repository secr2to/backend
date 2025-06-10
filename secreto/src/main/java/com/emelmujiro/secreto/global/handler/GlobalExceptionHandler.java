package com.emelmujiro.secreto.global.handler;

import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.emelmujiro.secreto.global.exception.ApiException;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<?>> handleCustomException(ApiException e, HttpServletRequest request) {
		log.info("========== [GLOBAL EXCEPTION] ==========\n" +
						"RequestURI: {}\nMessage: {}\n",
				request.getRequestURI(),
				e.getErrorCode().getMessage(), e);
		return ApiResponse.builder().error(e.getErrorCode());
	}

	// 정의하지 않은 시스템 에러(전역)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleAllException(Exception e, HttpServletRequest request) {
		log.error("========== [SYSTEM ERROR] ==========\n" +
						"RequestURI: {}\nMessage: {}\n",
				request.getRequestURI(),
				e.getMessage(), e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
