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
import io.jsonwebtoken.ExpiredJwtException;
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
		final Long userId = principal.getAttribute("userId");
		final String username = principal.getAttribute("username");
		final String provider = principal.getAttribute("provider");
		final String role = principal.getAuthorities().stream()
			.findFirst()
			.orElseThrow(IllegalAccessError::new)
			.getAuthority();

		assert userId != null && username != null && provider != null && role != null;
		final Map<String, Object> claims = new HashMap<>(Map.of(
			"username", username,
			"provider", provider,
			"role", role
		));

		final String refreshToken = generateRefreshToken(userId, claims);
		final String accessToken = generateAccessToken(userId, claims);

		return new AuthToken(refreshToken, accessToken);
	}

	private String generateRefreshToken(Long userId, Map<String, Object> claims) {
		claims.put("tokenType", TOKEN_TYPE_REFRESH);
		return buildToken(userId, claims, refreshTokenExpirationSeconds * 1000L);
	}

	private String generateAccessToken(Long userId, Map<String, Object> claims) {
		claims.put("tokenType", TOKEN_TYPE_ACCESS);
		return buildToken(userId, claims, accessTokenExpirationSeconds * 1000L);
	}

	public String generateAccessToken(User user) {
		final Map<String, Object> claims = new HashMap<>(Map.of(
			"username", user.getUsername(),
			"provider", user.getOAuthProvider(),
			"role", user.getRole()
		));
		return generateAccessToken(user.getId(), claims);
	}

	private String buildToken(Long userId, Map<String, Object> claims, long periodMillis) {
		Date now = new Date();
		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claims(claims)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + periodMillis))
			.signWith(secretKey)
			.compact();
	}

	public String resolveAuthorization(HttpServletRequest request) {
		String authorization = request.getHeader(AUTHORIZATION);
		if (!authorization.startsWith("Bearer ")) {
			throw new AuthException(AuthErrorCode.MISSING_BEARER_TOKEN);
		}
		return authorization.replaceFirst("Bearer ", "");
	}

	public boolean verifyToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);

			return claims.getPayload()
				.getExpiration()
				.after(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	public String getUsername(String token) {
		return getClaims(token).get("username", String.class);
	}

	public String getRole(String token) {
		return getClaims(token).get("role", String.class);
	}

	public String getProvider(String token) {
		return getClaims(token).get("provider", String.class);
	}

	public boolean isRefreshToken(String token) {
		try {
			return TOKEN_TYPE_REFRESH.equals(getClaims(token).get("tokenType", String.class));
		} catch (ExpiredJwtException e) {
			throw new AuthException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
		}
	}

	public boolean isAccessToken(String token) {
		try {
			return TOKEN_TYPE_ACCESS.equals(getClaims(token).get("tokenType", String.class));
		} catch (ExpiredJwtException e) {
			throw new AuthException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
		}
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
