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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		log.error("========== [AUTH ENTRY POINT] ==========\n" +
				"RequestURI: {}\nMessage: {}\n",
			request.getRequestURI(),
			exception.getMessage(), exception);

		FilterResponseWriter.of(response)
			.errorCode(CommonErrorCode.INTERNAL_SERVER_ERROR)
			.send();
	}
}
