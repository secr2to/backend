package com.emelmujiro.secreto.feed.dto.response;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public record GetCommunityResponseDto(
	Long feedId,
	String title,
	int imageCount,
	int heartCount,
	int replyCount,
	String thumbnailImageUrl,
	FeedUserResponseDto author,
	LocalDateTime createDate
) {

	@QueryProjection
	public GetCommunityResponseDto {
	}
}
