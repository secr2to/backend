package com.emelmujiro.secreto.feed.dto.response;

import com.emelmujiro.secreto.feed.entity.Feed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateFeedResponseDto {

	private Long feedId;

	public static CreateFeedResponseDto from(Feed feed) {
		return CreateFeedResponseDto.builder()
			.feedId(feed.getId())
			.build();
	}
}
