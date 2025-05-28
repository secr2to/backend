package com.emelmujiro.secreto.global.dto.response;

public record SuccessResponseDto(boolean success) {

	public static SuccessResponseDto ofSuccess() {
		return new SuccessResponseDto(true);
	}

	public static SuccessResponseDto ofFail() {
		return new SuccessResponseDto(false);
	}

	public static SuccessResponseDto of(boolean success) {
		return new SuccessResponseDto(success);
	}
}