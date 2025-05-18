package com.emelmujiro.secreto.feed.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record HeartResponseDto(
	int heartCount,
	boolean heart,
	String heartMessage
) {

	@QueryProjection
	public HeartResponseDto {
	}
}
