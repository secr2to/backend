package com.emelmujiro.secreto.auth.exception;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.global.error.ErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class AuthException extends ApiException {

	public AuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}

	public AuthException(AuthErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
