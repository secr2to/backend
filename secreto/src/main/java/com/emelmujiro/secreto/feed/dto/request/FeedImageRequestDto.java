package com.emelmujiro.secreto.feed.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImageRequestDto {

	private String imageUrl;
}
