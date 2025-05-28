package com.emelmujiro.secreto.feed.exception;

import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class FeedException extends ApiException {

	public FeedException(FeedErrorCode errorCode) {
		super(errorCode);
	}
}
