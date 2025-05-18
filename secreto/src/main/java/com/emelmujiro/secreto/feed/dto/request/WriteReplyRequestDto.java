package com.emelmujiro.secreto.feed.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WriteReplyRequestDto {

	@InjectUserId
	private Long userId;

	private Long feedId;
	private Long roomId;
	private Long parentReplyId;
	private String comment;
	private Long mentionUserId;
}
