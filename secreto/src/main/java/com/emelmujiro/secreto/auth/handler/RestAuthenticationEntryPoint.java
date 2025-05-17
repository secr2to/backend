package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.response.FilterResponseWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

		FilterResponseWriter.of(response)
			.errorCode(CommonErrorCode.INTERNAL_SERVER_ERROR)
			.send();
	}
}
