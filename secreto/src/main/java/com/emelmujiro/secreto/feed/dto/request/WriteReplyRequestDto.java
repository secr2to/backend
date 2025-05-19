package com.emelmujiro.secreto.feed.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WriteReplyRequestDto {

	@InjectUserId
	private Long userId;

	private Long feedId;
	private Long roomId;
	private Long parentReplyId;
	private String comment;
	private Long mentionUserId;
}
