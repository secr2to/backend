package com.emelmujiro.secreto.feed.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedApiMessage {

	GET_COMMUNITY_SUCCESS("커뮤니티 게시글 목록을 조회하였습니다. offset=%d, keyword='%s'"),
	GET_COMMUNITY_DETAIL_SUCCESS("커뮤니티 게시글을 조회하였습니다. id={}"),
	GET_INGAME_FEEDS_SUCCESS("인게임 게시글을 조회하였습니다."),
	CREATE_FEED_SUCCESS("게시글을 생성하였습니다."),
	UPDATE_FEED_SUCCESS("게시글을 수정하였습니다."),
	DELETE_FEED_SUCCESS("게시글을 삭제하였습니다."),
	;

	private final String message;
}
