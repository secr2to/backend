package com.emelmujiro.secreto.auth.exception;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class AuthException extends ApiException {

	public AuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}
}
