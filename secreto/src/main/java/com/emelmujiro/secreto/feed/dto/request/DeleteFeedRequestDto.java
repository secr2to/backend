package com.emelmujiro.secreto.feed.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteFeedRequestDto {

	@InjectPathVariable
	private Long feedId;

	@InjectPathVariable(required = false)
	private Long roomId;

	@InjectUserId
	private Long authorId;
}
