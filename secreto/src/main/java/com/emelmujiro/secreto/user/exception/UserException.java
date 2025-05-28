package com.emelmujiro.secreto.user.exception;

import com.emelmujiro.secreto.global.exception.ApiException;
import com.emelmujiro.secreto.user.error.UserErrorCode;

public class UserException extends ApiException {

	public UserException(UserErrorCode errorCode) {
		super(errorCode);
	}
}
