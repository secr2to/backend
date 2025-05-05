package com.emelmujiro.secreto.auth.error;

import org.springframework.http.HttpStatus;

import com.emelmujiro.secreto.global.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다. 다시 로그인해 주세요."),
	SESSION_INVALID(HttpStatus.BAD_REQUEST, "세션에 유효한 데이터가 없습니다."),
	KEY_UUID_INVALID(HttpStatus.BAD_REQUEST, "UUID에 해당하는 토큰을 찾을 수 없습니다."),
	DATA_CONVERSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증 과정에서 데이터 변환에 실패하였습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
