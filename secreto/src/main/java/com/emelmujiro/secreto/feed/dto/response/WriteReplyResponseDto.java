package com.emelmujiro.secreto.feed.dto.response;

import com.emelmujiro.secreto.feed.entity.FeedReply;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WriteReplyResponseDto {

	private Long replyId;

	public static WriteReplyResponseDto from(FeedReply reply) {
		return WriteReplyResponseDto.builder()
			.replyId(reply.getId())
			.build();
	}
}
