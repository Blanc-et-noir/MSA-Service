package com.spring.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfiguration {
	@Bean
	TemplateEngine httpTemplateEngine() {
		TemplateEngine templateEngine = new SpringTemplateEngine();
		
		templateEngine.addTemplateResolver(httpTemplateResolver());
		
		return templateEngine;
	}
	
	@Bean
	SpringResourceTemplateResolver httpTemplateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		
		templateResolver.setOrder(1);
		templateResolver.setPrefix("classpath:templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		
		return templateResolver;
	}
}
