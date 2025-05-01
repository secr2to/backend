package com.emelmujiro.secreto.global.response;

import static java.util.Objects.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private final T data;
	private final long timestamp;
	private final String message;

	private ApiResponse(T data, String message) {
		this.data = data;
		this.timestamp = System.currentTimeMillis();
		this.message = message;
	}

	public static <T> ApiResponseBuilder<T> builder() {
		return new ApiResponseBuilder<>();
	}

	public static class ApiResponseBuilder<T> {

		private static final ErrorCode failError = CommonErrorCode.VALIDATION_FAILED;
		private T data;
		private String message;
		private HttpHeaders headers = new HttpHeaders();
		private HttpStatus status = HttpStatus.OK;

		public ApiResponseBuilder() {
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		}

		public ApiResponseBuilder<T> data(T data) {
			this.data = data;
			return this;
		}

		public ApiResponseBuilder<T> message(String message) {
			this.message = message;
			return this;
		}

		public ApiResponseBuilder<T> headers(HttpHeaders headers) {
			this.headers = headers;
			return this;
		}

		public ApiResponseBuilder<T> status(HttpStatus status) {
			this.status = status;
			return this;
		}

		public ResponseEntity<ApiResponse<T>> success() {
			return new ResponseEntity<>(
				new ApiResponse<>(data, message),
				headers,
				status
			);
		}

		public ResponseEntity<ApiResponse<?>> error(ErrorCode errorCode) {
			return new ResponseEntity<>(
				new ApiResponse<>(data, errorCode.getMessage()),
				headers,
				errorCode.getHttpStatus()
			);
		}

		public ResponseEntity<ApiResponse<Map<String, String>>> fail(BindingResult bindingResult) {
			return new ResponseEntity<>(
				new ApiResponse<>(generateFailData(bindingResult), requireNonNull(message, failError.getMessage())),
				headers,
				failError.getHttpStatus());
		}

		private static Map<String, String> generateFailData(BindingResult bindingResult) {
			Map<String, String> errors = new HashMap<>();
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for (ObjectError error: allErrors) {
				if (error instanceof FieldError) {
					errors.put(((FieldError) error).getField(), error.getDefaultMessage());
				} else {
					errors.put(error.getObjectName(), error.getDefaultMessage());
				}
			}
			return errors;
		}
	}
}
