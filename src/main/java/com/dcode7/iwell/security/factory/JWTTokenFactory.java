package com.dcode7.iwell.security.factory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.dcode7.iwell.constant.IwellConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dcode7.iwell.security.login.JWTAuthenticationResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTTokenFactory {

	@Value("${jwt.expiration_ms}")
	private Long expirationTime;

	@Value("${jwt.secret}")
	private String tokenSecret;

	private SecretKey Key;

	public JWTTokenFactory() {
		if (tokenSecret == null) {
			tokenSecret = IwellConstant.JWT_SECRET;
		}
		if (expirationTime == null) {
			expirationTime = IwellConstant.JWT_EXPIRATION;
		}
		byte[] keyBytes = Base64.getDecoder().decode(tokenSecret.getBytes(StandardCharsets.UTF_8));
		this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	public String getSubjectFromJwtToken(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	public JWTAuthenticationResponse createTokenPair(String email) {
		String accessToken = createAccessToken(email);
		String refreshToken = createRefreshToken(email);
		return new JWTAuthenticationResponse(accessToken, refreshToken);
	}

	private String createAccessToken(String email) {
		System.out.println("Expiration: " + expirationTime);
		return Jwts.builder().subject(email).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(Key).compact();
	}

	public String createRefreshToken(String email) {

		return Jwts.builder().subject(email).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(Key).compact();
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
		return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
	}
}