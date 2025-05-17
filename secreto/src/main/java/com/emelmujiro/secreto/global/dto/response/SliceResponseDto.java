package com.emelmujiro.secreto.global.dto.response;

import java.util.List;

public record SliceResponseDto<T>(
	List<T> content,
	int offset,
	boolean hasNext
) {
}
