package com.emelmujiro.secreto.feed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetIngameFeedsRequestDto {

	private int offset;
	private Long roomId;
	private Long userId;
}
