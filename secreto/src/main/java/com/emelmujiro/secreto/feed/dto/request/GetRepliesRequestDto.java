package com.emelmujiro.secreto.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetRepliesRequestDto {

	private Long feedId;
	private Long roomId;
	private Long replyId;
	private Long userId;
	private int offset;
}
