package com.emelmujiro.secreto.global.response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.emelmujiro.secreto.global.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class FilterResponseWriter<T> {

	private final HttpServletResponse response;
	private int status = HttpStatus.OK.value();
	private String contentType = new MediaType("application", "json", StandardCharsets.UTF_8).toString();
	private T data = null;
	private String message = null;
	private ErrorCode errorCode = null;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private FilterResponseWriter(HttpServletResponse response) {
		this.response = response;
	}

	public static <T> FilterResponseWriter<T> of(HttpServletResponse response) {
		return new FilterResponseWriter<>(response);
	}

	public FilterResponseWriter<T> status(HttpStatus status) {
		this.status = status.value();
		return this;
	}

	public FilterResponseWriter<T> contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public FilterResponseWriter<T> data(T data) {
		this.data = data;
		return this;
	}

	public FilterResponseWriter<T> message(String message) {
		this.message = message;
		return this;
	}

	public FilterResponseWriter<T> errorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public void send() throws IOException {
		response.setStatus(errorCode != null ? errorCode.getHttpStatus().value() : status);
		response.setContentType(contentType);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(
			objectMapper.writeValueAsString(
				new ApiResponse<>(data, errorCode != null ? errorCode.getMessage() : message)
			)
		);
	}
}
