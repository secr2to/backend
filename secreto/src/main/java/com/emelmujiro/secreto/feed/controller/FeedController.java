package com.emelmujiro.secreto.feed.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.message.FeedApiMessage;
import com.emelmujiro.secreto.feed.service.FeedService;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;

	@PostMapping({"/community", "/rooms/{roomId}/feed"})
	public ResponseEntity<?> createFeed(@RequestBody CreateFeedRequestDto createFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.create(createFeedRequest))
			.message(FeedApiMessage.CREATE_FEED_SUCCESS.getMessage())
			.success();
	}
}
