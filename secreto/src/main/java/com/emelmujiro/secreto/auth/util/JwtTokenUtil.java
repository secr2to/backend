package com.emelmujiro.secreto.auth.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.auth.dto.AuthToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	@Value("${jwt.secret}")
	private String jwtSecret;

	private SecretKey secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public AuthToken generateToken(String email, String role) {
		String refreshToken = generateRefreshToken(email, role);
		String accessToken = generateAccessToken(email, role);

		/* === token 저장 로직 === */

		return new AuthToken(refreshToken, accessToken);
	}

	public String generateRefreshToken(String email, String role) {
		long refreshPeriod = 1000L * 60L * 60L * 24L * 7;
		Date now = new Date();

		return Jwts.builder()
			.subject(email)
			.claim("role", role)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + refreshPeriod))
			.signWith(secretKey)
			.compact();
	}

	public String generateAccessToken(String email, String role) {
		long tokenPeriod = 1000L * 60L * 30L;
		Date now = new Date();

		return Jwts.builder()
			.subject(email)
			.claim("role", role)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + tokenPeriod))
			.signWith(secretKey)
			.compact();
	}

	public boolean verifyToken(String token) {
		try {

			Jws<Claims> claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);

			System.out.println(claims);

			return claims.getBody()
				.getExpiration()
				.after(new Date());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	public String getSubject(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}

	public String getRole(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
	}
}
