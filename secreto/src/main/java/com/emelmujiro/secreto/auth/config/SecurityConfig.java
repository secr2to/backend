package com.emelmujiro.secreto.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.emelmujiro.secreto.auth.filter.JwtAuthenticationFilter;
import com.emelmujiro.secreto.auth.handler.MyAuthenticationFailureHandler;
import com.emelmujiro.secreto.auth.handler.MyAuthenticationSuccessHandler;
import com.emelmujiro.secreto.auth.handler.RestAuthenticationEntryPoint;
import com.emelmujiro.secreto.auth.service.CustomOAuth2UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final MyAuthenticationSuccessHandler authenticationSuccessHandler;
	private final MyAuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/token", "/auth/redirect").permitAll()
				.requestMatchers("/", "/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService))
				.loginPage("/")
				.failureHandler(authenticationFailureHandler)
				.successHandler(authenticationSuccessHandler)
			)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new RestAuthenticationEntryPoint()));

		http.logout(logout -> logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
			.addLogoutHandler(((request, response, authentication) -> {
				HttpSession session = request.getSession();
				session.invalidate();
			}))
			.logoutSuccessHandler((request, response, authentication) ->
				response.sendRedirect("/"))
		);

		http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
