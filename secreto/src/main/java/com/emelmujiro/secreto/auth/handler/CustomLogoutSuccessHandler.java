package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.message.AuthMessage;
import com.emelmujiro.secreto.global.response.FilterResponseWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		FilterResponseWriter.of(response)
			.message(AuthMessage.LOGOUT_SUCCESS.getMessage())
			.status(HttpStatus.OK)
			.send();
	}
}
