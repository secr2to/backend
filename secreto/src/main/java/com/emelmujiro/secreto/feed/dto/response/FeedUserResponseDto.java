package com.emelmujiro.secreto.feed.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record FeedUserResponseDto(
	Long userId,
	String searchId,
	String profileUrl
) {

	@QueryProjection
	public FeedUserResponseDto {
	}
}
