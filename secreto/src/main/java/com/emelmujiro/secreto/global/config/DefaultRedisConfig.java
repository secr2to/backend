package com.emelmujiro.secreto.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class DefaultRedisConfig extends RedisConfig {

	@Bean
	@Primary
	public LettuceConnectionFactory redisConnectionFactory() {
		return createConnectionFactory(0);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		return createRedisTemplate(redisConnectionFactory());
	}
}
