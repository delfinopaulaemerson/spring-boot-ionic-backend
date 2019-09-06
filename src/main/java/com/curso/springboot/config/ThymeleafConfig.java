package com.curso.springboot.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfig {

	@Bean
	public TemplateEngine htmlTemplateEngine(){
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.addTemplateResolver(htmlITemplateResolver());
		return templateEngine;
		
	}
	
	private ITemplateResolver htmlITemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("email/*"));
		templateResolver.setPrefix("/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF8");
		templateResolver.setCacheable(false);
		
		return templateResolver;
	}
}
