package com.Util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class Jwtutils {

	private static final String SECRECT_KEY = "A32wsRE54royaltechahmedabad2025india@$1234567890dkpqwerrrjklakshmi";
	private static final long EXPIRE_TIME = 60 * 60 * 60 * 1000;

	private Key getKey() {
		return Keys.hmacShaKeyFor(SECRECT_KEY.getBytes());
	}

	public String generateToken(String username, String role) {

		Map<String, Object> claims = new HashMap<>();
		System.out.println("username-->" + username);
		claims.put("role", role);
		claims.put("username", username);

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
//	        return subject.split("\\|")[0];
	}

	public String extractRole(String token) {
		System.out.println("role => " + Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token)
				.getBody().get("role", String.class));
		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("role",
				String.class);
	}

	public String extractId(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("id",
				String.class);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

}
