package com.emelmujiro.secreto.auth.util;

import java.util.Map;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;

public class ExceptionHandlingUtil {

	public static Map<String, Object> getAuthExceptionHandlingData(AuthException e) {
		Map<String, Object> data = null;
		if (e.getErrorCode() == AuthErrorCode.REFRESH_TOKEN_EXPIRED) {
			data = Map.of("tokenType", "refreshToken");
		} else if (e.getErrorCode() == AuthErrorCode.ACCESS_TOKEN_EXPIRED) {
			data = Map.of("tokenType", "accessToken");
		}
		return data;
	}
}
