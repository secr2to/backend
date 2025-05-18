package com.emelmujiro.secreto.global.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SliceResponseDto<T> {

	private List<T> content;
	private int offset;
	private boolean hasNext;
}
