package com.emelmujiro.secreto.global.advice;

import static org.springframework.web.servlet.HandlerMapping.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.emelmujiro.secreto.global.annotation.InjectPathVariable;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class PathVariableInjectionAdvice implements RequestBodyAdvice {

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {
		Class<?> clazz = methodParameter.getParameterType();
		return Arrays.stream(clazz.getDeclaredFields())
			.anyMatch(field -> field.isAnnotationPresent(InjectPathVariable.class));
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {

		HttpServletRequest request = getHttpServletRequest();
		Map<String, String> pathVariables = getPathVariables(request);
		injectPathVariableIntoAnnotatedFields(body, pathVariables);

		return body;
	}

	private void injectPathVariableIntoAnnotatedFields(Object body, Map<String, String> pathVariables) {
		Arrays.stream(body.getClass().getDeclaredFields())
			.filter(field -> field.isAnnotationPresent(InjectPathVariable.class))
			.forEach(field -> {
				InjectPathVariable annotation = field.getAnnotation(InjectPathVariable.class);
				String name = resolveFieldName(field);
				boolean required = annotation.required();

				if (!pathVariables.containsKey(name) && required) {
					throw new IllegalArgumentException(
						String.format("Path variable '%s' is required.", name)
					);
				}

				if (pathVariables.containsKey(name)) {
					Class<?> type = field.getType();
					Object value = getConvertedValue(pathVariables, name, type);

					ReflectionUtils.makeAccessible(field);
					ReflectionUtils.setField(field, body, value);
				}
			});
	}

	private Object getConvertedValue(Map<String, String> pathVariables, String name, Class<?> type) {
		Object converted;
		String value = pathVariables.get(name);
		try {
			if (Long.class.isAssignableFrom(type) || type == long.class) {
				converted = Long.parseLong(value);
			} else if (Integer.class.isAssignableFrom(type) || type == int.class) {
				converted = Integer.parseInt(value);
			} else if (Boolean.class.isAssignableFrom(type) || type == boolean.class) {
				converted = Boolean.parseBoolean(value);
			} else if (Double.class.isAssignableFrom(type) || type == double.class) {
				converted = Double.parseDouble(value);
			} else if (Float.class.isAssignableFrom(type) || type == float.class) {
				converted = Float.parseFloat(value);
			} else {
				converted = pathVariables.get(name);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
				String.format("Cannot convert path variable '%s' value '%s' to type %s.",
					name, value, type)
			);
		}
		return converted;
	}

	private String resolveFieldName(Field field) {
		String name = field.getAnnotation(InjectPathVariable.class).name();
		return (name == null || name.isEmpty()) ? field.getName() : name;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getPathVariables(HttpServletRequest httpServletRequest) {
		return (Map<String, String>) httpServletRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
	}

	private HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (!(requestAttributes instanceof ServletRequestAttributes)) {
			throw new IllegalStateException("No current HTTP request available.");
		}
		return ((ServletRequestAttributes) requestAttributes).getRequest();
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
