package com.generation.ammazzon.configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JwtService {


	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expirationMs;

	private Key key;

	@PostConstruct
	private void init() {
		// converte la stringa Base64 in chiave HMAC-SHA
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}


	public String generateToken(Authentication auth) {
		String roles = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Date now = new Date();
		Date exp = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				.setSubject(auth.getName())       // username
				.claim("roles", roles)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	/** Estrae lo username (subject) senza sollevare eccezioni in caso di token invalido./
	 public String extractUsername(String token) {
	 try { return parse(token).getSubject(); }
	 catch (Exception e) { return null; }
	 }

	 / Estrae i ruoli come lista di stringhe. */
	public List<String> extractRoles(String token) {
		try {
			String roles = (String) parse(token).get("roles");
			return List.of(roles.split(","));
		} catch (Exception e) {
			return List.of();
		}
	}


	public boolean isTokenValid(String token) {
		try {
			Claims c = parse(token);
			return c.getExpiration().after(new Date());
		} catch (Exception e) {
			return false;
		}
	}
	/* ---------- helper ---------- */

	private Claims parse(String token) {
		return Jwts.parser()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}