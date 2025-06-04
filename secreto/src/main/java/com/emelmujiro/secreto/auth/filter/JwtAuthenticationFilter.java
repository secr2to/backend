package com.emelmujiro.secreto.auth.filter;

import static com.emelmujiro.secreto.auth.config.SecurityConfig.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		String authorization = jwtTokenUtil.validateAccessToken(request, response);
		if (authorization == null) {
			return;
		}
		Long userId = jwtTokenUtil.getUserId(authorization);
		User findUser = userRepository.findById(userId)
			.orElseThrow(() -> new AuthException(AuthErrorCode.TOKEN_USER_MISSING));

		SecurityContextUser contextUser = SecurityContextUser.of(findUser);
		Authentication authentication = getAuthentication(contextUser);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	private Authentication getAuthentication(SecurityContextUser user) {
		return new UsernamePasswordAuthenticationToken(
			user,
			null,
			List.of(new SimpleGrantedAuthority(user.role().toString())));
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return Arrays.stream(WHITELIST_URLS)
			.anyMatch(path -> new AntPathRequestMatcher(path).matches(request));
	}
}
