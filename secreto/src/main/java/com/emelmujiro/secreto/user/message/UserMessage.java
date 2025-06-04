package com.emelmujiro.secreto.user.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserMessage {

	UPDATE_SEARCH_ID_SUCCESS("검색 ID를 수정하였습니다."),
		;

	private final String message;
}
