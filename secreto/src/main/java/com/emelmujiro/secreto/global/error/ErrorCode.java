package com.emelmujiro.secreto.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	HttpStatus getHttpStatus();
	String getMessage();
}
