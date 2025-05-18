package com.emelmujiro.secreto.feed.error;

import org.springframework.http.HttpStatus;

import com.emelmujiro.secreto.global.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ErrorCode {

	FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
	FEED_NOT_FOUND_OR_FORBIDDEN(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않거나 권한이 없습니다."),
	REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
	REPLY_NOT_FOUND_OR_FORBIDDEN(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않거나 권한이 없습니다."),
	PARENT_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "상위 댓글이 존재하지 않습니다."),
	SELF_MENTION_NOT_ALLOW(HttpStatus.BAD_REQUEST, "본인을 멘션할 수 없습니다."),
	IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "최소 1장 이상의 이미지를 첨부해주세요."),
	UNMATCHED_FEED_TYPE(HttpStatus.BAD_REQUEST, "게시글 타입이 일치하지 않습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
