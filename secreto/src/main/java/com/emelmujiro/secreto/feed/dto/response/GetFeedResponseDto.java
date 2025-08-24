package com.emelmujiro.secreto.feed.dto.response;

import java.util.Comparator;
import java.util.List;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedImage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetFeedResponseDto {

	private final Long feedId;
	private final String title;
	private final String content;
	private int imageCount;
	private List<FeedImageResponseDto> images;

	public static GetFeedResponseDto from(Feed feed) {
		return GetFeedResponseDto.builder()
			.feedId(feed.getId())
			.title(feed.getTitle())
			.content(feed.getContent())
			.imageCount(feed.getImages().size())
			.images(feed.getImages().stream()
				.map(FeedImageResponseDto::from)
				.sorted(Comparator.comparingInt(FeedImageResponseDto::getOrder))
				.toList())
			.build();
	}
}
