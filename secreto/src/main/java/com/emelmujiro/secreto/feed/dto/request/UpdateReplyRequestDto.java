package com.emelmujiro.secreto.feed.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateReplyRequestDto {

	@InjectUserId
	private Long replierId;

	@InjectPathVariable
	private Long replyId;

	private String comment;
}
