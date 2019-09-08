package com.curso.springboot.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private String expiration;
	
	Map<String, Object> claims = new HashMap<>();
	
	
	
	
	//geracao do token utilizando spring security
	@SuppressWarnings("deprecation")
	public String generateToken(String username) {
		String token = null;
		Long nowMillis = null;
		Date exp = null;
		
		nowMillis = System.currentTimeMillis()+ Long.valueOf(expiration);
		exp = new Date(nowMillis);
		
		Claims claims = Jwts.claims().setSubject(username);
			
		token = Jwts.builder().setClaims(claims).setExpiration(exp).signWith(SignatureAlgorithm.HS512, secret).compact();
		
		return token;
	}
	
	public boolean tokenValido(String token) {
		Claims claims = null;
		String username = null;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			if(claims != null) {
				username = claims.getSubject();
				//TUDO implementar a expiracao do token
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public String getUsername(String token) {
		Claims claims = null;
		String username = null;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			if(claims != null) {
				username = claims.getSubject();
				//TUDO implementar a expiracao do token
			}
		} catch (Exception e) {
			return username;
		}
		
		return username;
	}

}
