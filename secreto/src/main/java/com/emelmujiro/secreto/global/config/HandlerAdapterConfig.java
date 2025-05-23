package com.emelmujiro.secreto.global.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HandlerAdapterConfig {

	private final RequestMappingHandlerAdapter handlerAdapter;

	@PostConstruct
	private void removeDefaultResolvers() {
		if (handlerAdapter.getArgumentResolvers() == null) return;

		log.info("기본 ServletModelAttributeMethodProcessor 제거");
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(handlerAdapter.getArgumentResolvers());
		resolvers.removeIf(resolver ->
			resolver != null &&
			resolver.getClass() == ServletModelAttributeMethodProcessor.class
		);
		handlerAdapter.setArgumentResolvers(resolvers);
	}
}
