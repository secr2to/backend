package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("onAuthenticationSuccess");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

		log.info("oAuth2User={}", oAuth2User);

		AuthToken token = jwtTokenUtil.generateToken(oAuth2User);
		log.info("accessToken={}", token.getAccessToken());
		log.info("refreshToken={}", token.getRefreshToken());
		getRedirectStrategy().sendRedirect(request, response, "/auth/redirect?accessToken=" + token.getAccessToken() + "&refreshToken=" + token.getRefreshToken());
	}
}
