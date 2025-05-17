package com.emelmujiro.secreto.feed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCommunityRequestDto {

	private int offset;
	private String keyword;
}
