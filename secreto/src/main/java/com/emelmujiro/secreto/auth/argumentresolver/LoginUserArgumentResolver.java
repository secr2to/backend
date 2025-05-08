package com.emelmujiro.secreto.auth.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.auth.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("supportsParameter 실행");
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
		boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
		return hasLoginAnnotation && hasLongType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("resolveArgument 실행");

		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		String authorization = jwtTokenUtil.resolveAuthorization(request);

		if (authorization == null) {
			return null;
		}
		return jwtTokenUtil.getUserId(authorization);
	}
}
