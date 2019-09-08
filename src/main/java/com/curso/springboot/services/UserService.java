package com.curso.springboot.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.curso.springboot.security.UserSS;


public class UserService {
	
	public static UserSS authenticated() {
		UserSS userSS = null;
		try {
			userSS = (UserSS)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return userSS;
		} catch (Exception e) {
			return null;
		}
	}

}
