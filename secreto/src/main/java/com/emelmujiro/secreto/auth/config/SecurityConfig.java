package com.emelmujiro.secreto.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.emelmujiro.secreto.auth.filter.JwtAuthenticationFilter;
import com.emelmujiro.secreto.auth.handler.CustomAuthenticationFailureHandler;
import com.emelmujiro.secreto.auth.handler.CustomAuthenticationSuccessHandler;
import com.emelmujiro.secreto.auth.handler.RestAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	public static final String[] WHITELIST_URLS = {
		"/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/auth/**"
	};
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(WHITELIST_URLS).permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.userService(oAuth2UserService))
				.loginPage("/")
				.failureHandler(customAuthenticationFailureHandler)
				.successHandler(customAuthenticationSuccessHandler)
			)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(restAuthenticationEntryPoint));

		http.logout(logout -> logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
		);

		http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
