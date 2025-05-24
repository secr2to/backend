package com.emelmujiro.secreto.auth.advice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.util.SecurityContextUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserIdInjectionAdvice implements RequestBodyAdvice {

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {

		Authentication authentication = SecurityContextUtil.getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			SecurityContextUser principal = SecurityContextUtil.getPrincipal();
			Long userId = principal.userId();
			injectUserIdIntoAnnotatedFields(body, userId);
		}
		return body;
	}

	private void injectUserIdIntoAnnotatedFields(Object body, Long userId) {
		Arrays.stream(body.getClass().getDeclaredFields())
			.filter(this::isLoginUserField)
			.forEach(field -> {
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, body, userId);
			});
	}

	private boolean isLoginUserField(Field field) {
		boolean hasAnnotation = field.isAnnotationPresent(LoginUser.class);
		boolean hasLongType = Long.class.isAssignableFrom(field.getType()) || field.getType() == long.class;
		return hasAnnotation && hasLongType;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
		Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		return inputMessage;
	}
}
