package com.emelmujiro.secreto.feed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GetRepliesRequestDto {

	private Long feedId;
	private Long roomId;
	private Long replyId;
	private Long userId;
	private int offset;
}
