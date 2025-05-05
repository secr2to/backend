package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(AuthErrorCode.UNAUTHORIZED.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(
			new ObjectMapper().writeValueAsString(
				new ApiResponse<>(
					null,
					AuthErrorCode.UNAUTHORIZED.getMessage()
				)
			)
		);
	}
}
