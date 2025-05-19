package com.emelmujiro.secreto.feed.dto.request;

import java.util.List;

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
public class UpdateFeedRequestDto {

	@InjectUserId
	private Long authorId;

	@InjectPathVariable
	private Long feedId;

	private String title;
	private String content;
	private List<FeedImageRequestDto> images;
	private List<FeedTagRequestDto> tags;

	@InjectPathVariable(required = false)
	private Long roomId;
}
