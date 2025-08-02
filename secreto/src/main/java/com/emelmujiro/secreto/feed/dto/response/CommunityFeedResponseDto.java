package com.emelmujiro.secreto.feed.dto.response;

import java.time.LocalDateTime;

import com.emelmujiro.secreto.user.dto.response.UserProfileResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CommunityFeedResponseDto {
	private final Long feedId;
	private final String title;
	private final int imageCount;
	private final int heartCount;
	private final int replyCount;

	@JsonIgnore
	private final String thumbnailImageKey;
	private final UserProfileResponseDto author;
	private final LocalDateTime createDate;

	@Setter
	private String thumbnailImageUrl;

	@QueryProjection
	public CommunityFeedResponseDto(Long feedId, String title, int imageCount, int heartCount, int replyCount,
		String thumbnailImageKey, UserProfileResponseDto author, LocalDateTime createDate) {
		this.feedId = feedId;
		this.title = title;
		this.imageCount = imageCount;
		this.heartCount = heartCount;
		this.replyCount = replyCount;
		this.thumbnailImageKey = thumbnailImageKey;
		this.author = author;
		this.createDate = createDate;
	}
}
