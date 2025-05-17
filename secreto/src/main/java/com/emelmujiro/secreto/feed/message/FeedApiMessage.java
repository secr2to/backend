package com.emelmujiro.secreto.feed.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedApiMessage {

	CREATE_FEED_SUCCESS("게시글을 생성하였습니다."),
	UPDATE_FEED_SUCCESS("게시글을 수정하였습니다."),
	;

	private final String message;
}
