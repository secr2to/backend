package com.emelmujiro.secreto.auth.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthMessage {

	LOGOUT_SUCCESS("로그아웃에 성공하였습니다."),
		;

	private final String message;
}
