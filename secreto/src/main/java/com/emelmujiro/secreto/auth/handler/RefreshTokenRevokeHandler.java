package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.service.AuthTokenService;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenRevokeHandler implements LogoutHandler {

	private final JwtTokenUtil jwtTokenUtil;
	private final AuthTokenService authTokenService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
			String authorization = jwtTokenUtil.validateAccessToken(request, response);
			Long userId = jwtTokenUtil.getUserId(authorization);
			authTokenService.deleteRefreshToken(userId);
		} catch (IOException ignored) {
		}
	}
}
