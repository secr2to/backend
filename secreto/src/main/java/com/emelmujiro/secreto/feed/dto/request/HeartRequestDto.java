package com.emelmujiro.secreto.feed.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class HeartRequestDto {

	private Long feedId;
	private Long roomId;
	private Long userId;
}
