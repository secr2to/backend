package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.global.response.FilterResponseWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

		exception.printStackTrace();
		FilterResponseWriter.of(response)
			.errorCode(AuthErrorCode.UNAUTHORIZED)
			.send();
	}
}
