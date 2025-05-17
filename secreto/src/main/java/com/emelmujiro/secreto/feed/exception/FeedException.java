package com.emelmujiro.secreto.feed.exception;

import com.emelmujiro.secreto.global.error.ErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class FeedException extends ApiException {

	public FeedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
