package com.emelmujiro.secreto.feed.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedApiMessage {

	GET_COMMUNITY_SUCCESS("커뮤니티 게시글 목록을 조회하였습니다. offset=%d, keyword='%s'"),
	GET_COMMUNITY_FEED_SUCCESS("커뮤니티 게시글을 조회하였습니다. id={}"),
	GET_INGAME_FEEDS_SUCCESS("인게임 게시글을 조회하였습니다."),
	CREATE_FEED_SUCCESS("게시글을 생성하였습니다."),
	UPDATE_FEED_SUCCESS("게시글을 수정하였습니다."),
	DELETE_FEED_SUCCESS("게시글을 삭제하였습니다."),
	HEART_SUCCESS("'좋아요'를 했습니다."),
	UNHEART_SUCCESS("'좋아요'를 취소했습니다."),
	GET_REPLIES_SUCCESS("댓글 목록을 조회하였습니다. feedId=%d"),
	GET_NESTED_REPLIES_SUCCESS("댓글 목록을 조회하였습니다. feedId=%d, replyId=%d"),
	WRITE_REPLY_SUCCESS("댓글을 작성하였습니다."),
	UPDATE_REPLY_SUCCESS("댓글을 수정하였습니다."),
	DELETE_REPLY_SUCCESS("댓글을 삭제하였습니다."),
	HEART_MESSAGE("%s 외 %d명이 좋아합니다."),
	;

	private final String message;
}
