package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.global.response.FilterResponseWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		FilterResponseWriter.of(response)
			.errorCode(AuthErrorCode.UNAUTHORIZED)
			.send();
	}
}
