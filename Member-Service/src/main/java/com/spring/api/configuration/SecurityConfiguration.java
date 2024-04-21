package com.spring.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
		.httpBasic(httpBasic->{
			httpBasic.disable();
		})
		.csrf(csrf->{
			csrf.disable();
		})
		.cors(cors->{
			cors.disable();
		})
		.formLogin(form->{
			form.disable();
		})
		.logout(logout->{
			logout.disable();
		})
		.authorizeHttpRequests(auth->{			
			auth
				.anyRequest().permitAll();
		})
		.sessionManagement(session->{
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		})
		.build();
	}
	
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return web ->{
			web
				.ignoring()
				.requestMatchers("/**");
		};
	}
}
