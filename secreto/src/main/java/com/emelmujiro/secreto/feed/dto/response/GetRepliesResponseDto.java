package com.emelmujiro.secreto.feed.dto.response;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.global.dto.response.SliceResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GetRepliesResponseDto extends SliceResponseDto<ReplyResponseDto> {

	private ReplyFeedInfoResponseDto feedInfo;

	public void setFeed(Feed feed) {
		feedInfo = ReplyFeedInfoResponseDto.builder()
			.feedId(feed.getId())
			.replyCount(feed.getReplyCount())
			.heartCount(feed.getHeartCount())
			.build();
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class ReplyFeedInfoResponseDto {

		private Long feedId;
		private int replyCount;
		private int heartCount;
	}
}
