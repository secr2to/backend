package com.emelmujiro.secreto.auth.util;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.dto.AuthToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	private static final long ACCESS_TOKEN_PERIOD_MILLIS = 1000L * 60L * 30L;
	private static final long REFRESH_TOKEN_PERIOD_MILLIS = 1000L * 60L * 60L * 24L * 7;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private SecretKey secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public AuthToken generateToken(OAuth2User principal) {
		final String email = principal.getAttribute("email");
		final Long userId = principal.getAttribute("userId");
		final String provider = principal.getAttribute("provider");
		final String role = principal.getAuthorities().stream()
			.findFirst()
			.orElseThrow(IllegalAccessError::new)
			.getAuthority();

		assert userId != null && provider != null;
		final Map<String, Object> claims = Map.of(
			"userId", userId,
			"provider", provider,
			"role", role
		);

		final String refreshToken = generateRefreshToken(email, claims);
		final String accessToken = generateAccessToken(email, claims);

		return new AuthToken(refreshToken, accessToken);
	}

	private String generateRefreshToken(String subject, Map<String, Object> claims) {
		return buildToken(subject, claims, REFRESH_TOKEN_PERIOD_MILLIS);
	}

	private String generateAccessToken(String subject, Map<String, Object> claims) {
		return buildToken(subject, claims, ACCESS_TOKEN_PERIOD_MILLIS);
	}

	private String buildToken(String subject, Map<String, Object> claims, long periodMillis) {
		Date now = new Date();
		return Jwts.builder()
			.subject(subject)
			.claims(claims)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + periodMillis))
			.signWith(secretKey)
			.compact();
	}

	public String resolveAuthorization(HttpServletRequest request) {
		return request.getHeader("Authorization").replaceFirst("BEARER ", "");
	}

	public boolean verifyToken(String token) {
		try {

			Jws<Claims> claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);

			return claims.getBody()
				.getExpiration()
				.after(new Date());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public String getSubject(String token) {
		return getClaims(token).getSubject();
	}

	public Long getUserId(String token) {
		return getClaims(token).get("userId", Long.class);
	}

	public String getRole(String token) {
		return getClaims(token).get("role", String.class);
	}

	public String getProvider(String token) {
		return getClaims(token).get("provider", String.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
