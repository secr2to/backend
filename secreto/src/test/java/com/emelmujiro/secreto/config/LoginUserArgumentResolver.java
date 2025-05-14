package com.emelmujiro.secreto.config;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @LoginUser 애노테이션이 달린 Long 타입 파라미터를 지원
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        // 실제 로그인된 사용자의 ID를 추출하는 로직 (여기서는 예시로 1L 반환)
        // 실무에서는 SecurityContextHolder 또는 세션에서 ID를 꺼냄
        return 1L; // 예시: 항상 1L 반환
    }
}