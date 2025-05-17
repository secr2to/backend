package com.emelmujiro.secreto.feed.dto.request;

import java.util.List;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;

import lombok.Data;

@Data
public class CreateFeedRequestDto {

	@InjectUserId
	private Long authorId;

	private String title;
	private String content;
	private List<FeedImageRequestDto> images;
	private List<FeedTagRequestDto> tags;

	@InjectPathVariable(required = false)
	private Long roomId;
}
