package com.emelmujiro.secreto.auth.argumentresolver;

import static com.emelmujiro.secreto.auth.util.SecurityContextUtil.*;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.emelmujiro.secreto.auth.annotation.LoginUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("supportsParameter 실행");
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
		boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType()) ||
			parameter.getParameterType() == long.class;
		return hasLoginAnnotation && hasLongType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("resolveArgument 실행");

		Authentication authentication = getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		return getPrincipal().userId();
	}
}
