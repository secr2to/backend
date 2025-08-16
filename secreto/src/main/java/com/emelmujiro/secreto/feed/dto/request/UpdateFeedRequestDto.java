package com.emelmujiro.secreto.feed.dto.request;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateFeedRequestDto {

	@LoginUser
	private Long authorId;

	private Long feedId;

	private String title;
	private String content;
	private List<FeedImageRequestDto> images;
	private List<FeedTagRequestDto> tags;
}
