package com.emelmujiro.secreto.feed.dto.response;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

/**
 * TODO: 리팩토링 예정
 */
@Data
public class ReplyResponseDto {

	private Long replyId;
	private String comment;
	private LocalDateTime createDate;

	private boolean nestedReplyYn;
	private int nestedReplyCount;
	private int heartCount;
	private boolean heart;
	private FeedUserResponseDto replier;

	@QueryProjection
	public ReplyResponseDto(Long replyId, String comment, LocalDateTime createDate, boolean nestedReplyYn,
		int nestedReplyCount, int heartCount, boolean heart, FeedUserResponseDto replier) {
		this.replyId = replyId;
		this.comment = comment;
		this.createDate = createDate;
		this.nestedReplyYn = nestedReplyYn;
		this.nestedReplyCount = nestedReplyCount;
		this.heartCount = heartCount;
		this.heart = heart;
		this.replier = replier;
	}
}
