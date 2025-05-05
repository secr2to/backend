package com.emelmujiro.secreto.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.emelmujiro.secreto.global.config.RedisConfig;

@Configuration
public class TokenRedisConfig extends RedisConfig {

	@Bean
	public LettuceConnectionFactory authTokenRedisConnectionFactory() {
		return createConnectionFactory(1);
	}

	@Bean
	public LettuceConnectionFactory refreshTokenRedisConnectionFactory() {
		return createConnectionFactory(2);
	}

	@Bean
	public StringRedisTemplate authTokenRedisTemplate() {
		return createStringRedisTemplate(authTokenRedisConnectionFactory());
	}

	@Bean
	public StringRedisTemplate refreshTokenRedisTemplate() {
		return createStringRedisTemplate(refreshTokenRedisConnectionFactory());
	}
}
