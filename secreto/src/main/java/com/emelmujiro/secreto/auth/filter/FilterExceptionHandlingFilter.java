package com.emelmujiro.secreto.auth.filter;

import static com.emelmujiro.secreto.auth.util.ExceptionHandlingUtil.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.filter.OncePerRequestFilter;

import com.emelmujiro.secreto.auth.error.AuthErrorCode;
import com.emelmujiro.secreto.auth.exception.AuthException;
import com.emelmujiro.secreto.auth.util.ExceptionHandlingUtil;
import com.emelmujiro.secreto.global.error.CommonErrorCode;
import com.emelmujiro.secreto.global.response.FilterResponseWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterExceptionHandlingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (AuthException e) {
			FilterResponseWriter.of(response)
				.data(getAuthExceptionHandlingData(e))
				.errorCode(e.getErrorCode())
				.send();
		} catch (Exception e) {
			FilterResponseWriter.of(response)
				.errorCode(CommonErrorCode.INTERNAL_SERVER_ERROR)
				.send();
		}
	}
}
