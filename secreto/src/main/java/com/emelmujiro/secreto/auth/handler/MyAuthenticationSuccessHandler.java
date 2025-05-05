package com.emelmujiro.secreto.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.auth.service.AuthTokenService;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;

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
	private final AuthTokenService authTokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("onAuthenticationSuccess");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		boolean isNewUser = Boolean.TRUE.equals(oAuth2User.getAttribute("isNewUser"));
		Long userId = oAuth2User.getAttribute("userId");

		log.info("oAuth2User={}", oAuth2User);

		AuthToken token = jwtTokenUtil.generateToken(oAuth2User);
		log.info("accessToken={}", token.getAccessToken());
		log.info("refreshToken={}", token.getRefreshToken());
		String uuid = authTokenService.saveAuthToken(token);
		authTokenService.saveRefreshToken(userId, token.getRefreshToken());

		String redirectUrl = UriComponentsBuilder.fromPath("/auth/redirect")
			.queryParam("tempId", uuid)
			.queryParam("isNewUser", isNewUser)
			.build()
			.toUriString();

		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
