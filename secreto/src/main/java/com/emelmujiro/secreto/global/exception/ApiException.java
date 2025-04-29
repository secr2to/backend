package com.emelmujiro.secreto.global.exception;

import com.emelmujiro.secreto.global.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

	private final ErrorCode errorCode;
}
