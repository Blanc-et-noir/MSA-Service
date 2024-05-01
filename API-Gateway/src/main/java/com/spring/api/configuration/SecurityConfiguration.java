package com.spring.api.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.spring.api.filter.CacheRequestFilter;
import com.spring.api.filter.LogFilter;
import com.spring.api.filter.TokenAuthenticationFilter;
import com.spring.api.util.RedisUtil;
import com.spring.api.util.TokenUtil;

import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
	private final String BASE_URI="/api/v1";
	
	private String[] PERMITTED_GET_REQUESTS={
		"/favicon.ico",
		"/css/**",
		"/js/**",
		"/images/**",
		"/font/**",
		"/svg/**",
		BASE_URI+"/members/member-emails/{member-emails}",
		BASE_URI+"/tokens",
		BASE_URI+"/books",
		BASE_URI+"/books/{book-ids}/book-images/{book-image-ids}"
	};
	
	private String[] PERMITTED_POST_REQUESTS={
		BASE_URI+"/members",
		BASE_URI+"/tokens",
		BASE_URI+"/verifications/member-emails",
		BASE_URI+"/verifications/member-pws",
	};
	
	//private String[] PERMITTED_PATCH_REQUESTS={BASE_URI+"/tokens"};
	
	private String[] PERMITTED_PUT_REQUESTS={
		BASE_URI+"/tokens"
	};
	
	private String[] PERMITTED_DELETE_REQUESTS={
			BASE_URI+"/verifications/member-emails",
			BASE_URI+"/verifications/member-pws",
		};
	
	private String[] PERMITTED_VIEWS={
		BASE_URI+"/views/*"
	};
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}	
	
	@Bean
	SecurityWebFilterChain securityWebFilterChain(
			ServerHttpSecurity http,
			RestTemplate restTemplate,
			TokenUtil tokenUtil,
			RedisUtil redisUtil,
			CorsConfigurationSource corsConfigurationSource
			) throws Exception {
		
		
		return http
		.httpBasic(httpBasic->{
			httpBasic.disable();
		})
		.csrf(csrf->{
			csrf.disable();
		})
		.cors(cors->{
			cors.configurationSource(corsConfigurationSource);
		})
		.formLogin(form->{
			form.disable();
		})
		.logout(logout->{
			logout.disable();
		})
		.securityMatcher(				
			new AndServerWebExchangeMatcher(
				new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET,PERMITTED_GET_REQUESTS)),
				new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST,PERMITTED_POST_REQUESTS)),
				new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.PUT,PERMITTED_PUT_REQUESTS)),
				//new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.PATCH,PERMITTED_PATCH_REQUESTS)),
				new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.DELETE,PERMITTED_DELETE_REQUESTS)),
				new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET,PERMITTED_VIEWS))
			)
		)
		.authorizeExchange(auth->{			
			auth
				.pathMatchers(HttpMethod.GET, PERMITTED_GET_REQUESTS).permitAll()
				.pathMatchers(HttpMethod.POST, PERMITTED_POST_REQUESTS).permitAll()
				.pathMatchers(HttpMethod.PUT, PERMITTED_POST_REQUESTS).permitAll()
				//.pathMatchers(HttpMethod.PATCH, PERMITTED_PATCH_REQUESTS).permitAll()
				.pathMatchers(HttpMethod.DELETE, PERMITTED_DELETE_REQUESTS).permitAll()
				.pathMatchers(HttpMethod.GET, PERMITTED_VIEWS).permitAll()
				.anyExchange().authenticated();
		})
		.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
		.addFilterAt(new CacheRequestFilter(), SecurityWebFiltersOrder.FIRST)
		.addFilterAt(new LogFilter(restTemplate, tokenUtil), SecurityWebFiltersOrder.FIRST)
		.addFilterAt(new TokenAuthenticationFilter(redisUtil, tokenUtil), SecurityWebFiltersOrder.HTTP_BASIC)
		.build();
	}
	
	@Bean
	HttpClient httpClient() {
	    return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD","GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
