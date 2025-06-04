package com.emelmujiro.secreto.auth.error;

import org.springframework.http.HttpStatus;

import com.emelmujiro.secreto.global.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	MISSING_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "인증 헤더가 존재하지 않습니다."),
	MISSING_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "Bearer 토큰이 존재하지 않습니다."),
	WRONG_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "잘못된 토큰 타입입니다."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다. 다시 로그인해 주세요."),
	SESSION_INVALID(HttpStatus.BAD_REQUEST, "세션에 유효한 데이터가 없습니다."),
	KEY_UUID_INVALID(HttpStatus.BAD_REQUEST, "UUID에 해당하는 토큰을 찾을 수 없습니다."),
	DATA_CONVERSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증 과정에서 데이터 변환에 실패하였습니다."),
	TOKEN_USER_MISSING(HttpStatus.UNAUTHORIZED, "토큰의 유저 정보가 유효하지 않습니다."),
	LOGOUT_FAILURE(HttpStatus.UNAUTHORIZED, "로그아웃에 실패하였습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
