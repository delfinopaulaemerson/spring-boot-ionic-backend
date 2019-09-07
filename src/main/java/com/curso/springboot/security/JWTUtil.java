package com.curso.springboot.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

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
	public String generateToken(String username) {
		String token = null;
			Claims claims = Jwts.claims().setSubject(username);
			
			//TUDO adicionar a expiracao do token
			token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
		
		
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
