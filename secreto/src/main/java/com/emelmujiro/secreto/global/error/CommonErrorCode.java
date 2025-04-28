package com.emelmujiro.secreto.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
	LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Location could not be found"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
