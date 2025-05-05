package com.emelmujiro.secreto.auth.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.dto.AuthToken;
import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	private static final String TOKEN_TYPE_ACCESS = "access";
	private static final String TOKEN_TYPE_REFRESH = "refresh";
	private static final String AUTHORIZATION = "Authorization";

	@Value("${auth.access-token-expiration-seconds}")
	private long accessTokenExpirationSeconds;

	@Value("${auth.refresh-token-expiration-seconds}")
	private long refreshTokenExpirationSeconds;

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
		final Map<String, Object> claims = new HashMap<>(Map.of(
			"userId", userId,
			"provider", provider,
			"role", role
		));

		final String refreshToken = generateRefreshToken(email, claims);
		final String accessToken = generateAccessToken(email, claims);

		return new AuthToken(refreshToken, accessToken);
	}

	private String generateRefreshToken(String subject, Map<String, Object> claims) {
		claims.put("tokenType", TOKEN_TYPE_REFRESH);
		return buildToken(subject, claims, refreshTokenExpirationSeconds * 1000L);
	}

	private String generateAccessToken(String subject, Map<String, Object> claims) {
		claims.put("tokenType", TOKEN_TYPE_ACCESS);
		return buildToken(subject, claims, accessTokenExpirationSeconds * 1000L);
	}

	public String generateAccessToken(User user) {
		final Map<String, Object> claims = new HashMap<>(Map.of(
			"userId", user.getId(),
			"provider", user.getOAuthProvider(),
			"role", "ROLE_USER"
		));
		return generateAccessToken(user.getEmail(), claims);
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
		String authorization = request.getHeader(AUTHORIZATION);
		return authorization != null ? authorization.replaceFirst("Bearer ", "") : null;
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

	public boolean isRefreshToken(String token) {
		return TOKEN_TYPE_REFRESH.equals(getClaims(token).get("tokenType", String.class));
	}

	public boolean isAccessToken(String token) {
		return TOKEN_TYPE_ACCESS.equals(getClaims(token).get("tokenType", String.class));
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (SignatureException e) {
			throw new AuthException(AuthErrorCode.INVALID_TOKEN);
		}
	}
}
