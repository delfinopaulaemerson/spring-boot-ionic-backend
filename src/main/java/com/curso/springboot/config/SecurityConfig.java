package com.curso.springboot.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.curso.springboot.security.JWTAuthenticationFilter;
import com.curso.springboot.security.JWTAutorizationFilter;
import com.curso.springboot.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	public final static String[] PUBLIC_MATCHERS = {"/h2-console/**"};
	
	public final static String[] PUBLIC_MATCHERS_GET = {"/produtos/**","/categorias/**","/estados/**"};
	
	public final static String[] PUBLIC_MATCHERS_POST = {"/clientes/**","/auth/forgot/**"};
	
	@Override
	protected void configure(HttpSecurity http)throws Exception{
		
		if(Arrays.asList(this.environment.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		http.cors().and().csrf().disable();
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET,PUBLIC_MATCHERS_GET).permitAll()
		.antMatchers(HttpMethod.POST,PUBLIC_MATCHERS_POST).permitAll()
		.antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();
		
		//adicionando o filtro de login
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), this.jwtUtil));
		
		http.addFilter(new JWTAutorizationFilter(authenticationManager(), this.jwtUtil,this.userDetailsService ));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	 @Bean
	 CorsConfigurationSource corsConfigurationSource() {
		 CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		 configuration.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE","OPTIONS"));
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**",configuration);
	    return source;
	  }
	 
	 //adicionando senha ao cliente
	 @Bean
	 public BCryptPasswordEncoder bCryptPasswordEncoder() {
		 
		 return new BCryptPasswordEncoder();
	 }

}
