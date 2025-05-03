package com.emelmujiro.secreto.auth.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
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
		String authorization = request.getHeader("Authorization");
		log.info("Authorization={}", authorization);

		if (!StringUtils.hasText(authorization)) {
			doFilter(request, response, filterChain);
			return;
		}

		log.info("doFilterInternal");
		if (jwtTokenUtil.verifyToken(authorization)) {
			log.info("verifyToken=true");
			User findUser = userRepository.findByEmail(jwtTokenUtil.getSubject(authorization))
				.orElseThrow(IllegalStateException::new);

			SecurityContextUser contextUser = SecurityContextUser.of(findUser);

			log.info("contextUser={}", contextUser);
			Authentication authentication = getAuthentication(contextUser);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		/* JWT 인증이 완료되지 않았을 시, 예외 처리 필요 */
		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(SecurityContextUser user) {
		return new UsernamePasswordAuthenticationToken(
			user,
			null,
			List.of(new SimpleGrantedAuthority("ROLE_USER")));
	}
}
