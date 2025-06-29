package com.emelmujiro.secreto.auth.handler;

import static com.emelmujiro.secreto.auth.util.ExceptionHandlingUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ApiResponse<?>> handleCustomException(AuthException e, HttpServletRequest request) {
		log.error("========== [AUTH EXCEPTION] ==========\n" +
				"RequestURI: {}\nMessage: {}\n",
			request.getRequestURI(),
			e.getErrorCode().getMessage(), e);

		return ApiResponse.builder()
			.data(getAuthExceptionHandlingData(e))
			.error(e.getErrorCode());
	}
}