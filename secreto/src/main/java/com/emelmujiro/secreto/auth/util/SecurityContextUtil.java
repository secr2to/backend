package com.emelmujiro.secreto.auth.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;

public class SecurityContextUtil {

	public static SecurityContextUser getPrincipal() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!(principal instanceof SecurityContextUser)) {
			throw new AuthException(AuthErrorCode.UNAUTHORIZED);
		}
		return (SecurityContextUser) principal;
	}
}
