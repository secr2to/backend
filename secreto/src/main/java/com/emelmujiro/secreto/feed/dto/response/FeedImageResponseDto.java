package com.emelmujiro.secreto.feed.dto.response;

import com.emelmujiro.secreto.feed.entity.FeedImage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class FeedImageResponseDto {

	private Long imageId;
	private String imageUrl;

	public static FeedImageResponseDto from(FeedImage image) {
		return FeedImageResponseDto.builder()
			.imageId(image.getId())
			.imageUrl(image.getImageUrl())
			.build();
	}
}
