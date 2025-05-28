package com.emelmujiro.secreto.auth.argumentresolver;

import java.util.Arrays;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.auth.util.SecurityContextUtil;

public class LoginUserModelAttributeProcessor extends ServletModelAttributeMethodProcessor {

	public LoginUserModelAttributeProcessor(boolean annotationNotRequired) {
		super(annotationNotRequired);
	}

	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		super.bindRequestParameters(binder, request);
		injectLoginUserId(binder.getTarget());
	}

	private void injectLoginUserId(Object target) {
		if (target == null) return;

		Long userId = SecurityContextUtil.getPrincipal().userId();
		Arrays.stream(target.getClass().getDeclaredFields())
			.filter(field -> field.isAnnotationPresent(LoginUser.class))
			.forEach(field -> {
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, target, userId);
			});
	}
}
