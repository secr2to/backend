package com.emelmujiro.secreto.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

	ROLE_USER("유저"),
	ROLE_ADMIN("관리자"),
	;

	private final String name;
}
