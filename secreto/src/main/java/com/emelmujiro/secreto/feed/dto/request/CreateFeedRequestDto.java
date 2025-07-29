package com.emelmujiro.secreto.feed.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.emelmujiro.secreto.auth.annotation.LoginUser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateFeedRequestDto {

	@LoginUser
	private Long authorId;

	private String title;
	private String content;
	private List<MultipartFile> images;
	private List<FeedTagRequestDto> tags;

	private Long roomId;
}
