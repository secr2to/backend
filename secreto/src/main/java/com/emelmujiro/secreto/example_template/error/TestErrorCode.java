package com.emelmujiro.secreto.example_template.error;

import org.springframework.http.HttpStatus;

import com.emelmujiro.secreto.global.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TestErrorCode implements ErrorCode {

	TEST_ERROR(HttpStatus.NOT_FOUND, "테스트 에러"),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
