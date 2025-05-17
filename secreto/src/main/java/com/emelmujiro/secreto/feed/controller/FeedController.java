package com.emelmujiro.secreto.feed.controller;

import static java.lang.String.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.DeleteFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateFeedRequestDto;
import com.emelmujiro.secreto.feed.message.FeedApiMessage;
import com.emelmujiro.secreto.feed.service.FeedService;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;

	@GetMapping("/community")
	public ResponseEntity<?> getCommunity(@ModelAttribute GetCommunityRequestDto getFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.getCommunity(getFeedRequest))
			.message(format(FeedApiMessage.GET_COMMUNITY_SUCCESS.getMessage(),
				getFeedRequest.getOffset(),
				getFeedRequest.getKeyword())
			)
			.success();
	}

	@GetMapping("/community/{feedId}")
	public ResponseEntity<?> getCommunityDetail() {
		return ApiResponse.builder()
			.data(null)
			.message(format(FeedApiMessage.GET_COMMUNITY_DETAIL_SUCCESS.getMessage(), "id"))
			.success();
	}

	@GetMapping("/rooms/{roomId}/feeds")
	public ResponseEntity<?> getIngameFeeds() {
		return ApiResponse.builder()
			.data(null)
			.message(format(FeedApiMessage.GET_INGAME_FEEDS_SUCCESS.getMessage(), "page", "keyword"))
			.success();
	}

	@PostMapping({"/community", "/rooms/{roomId}/feeds"})
	public ResponseEntity<?> createFeed(@RequestBody CreateFeedRequestDto createFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.create(createFeedRequest))
			.message(FeedApiMessage.CREATE_FEED_SUCCESS.getMessage())
			.success();
	}

	@PutMapping({"/community/{feedId}", "/rooms/{roomId}/feeds/{feedId}"})
	public ResponseEntity<?> updateFeed(@RequestBody UpdateFeedRequestDto updateFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.update(updateFeedRequest))
			.message(FeedApiMessage.UPDATE_FEED_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping({"/community/{feedId}", "/rooms/{roomId}/feeds/{feedId}"})
	public ResponseEntity<?> deleteFeed(@RequestBody DeleteFeedRequestDto deleteFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.delete(deleteFeedRequest))
			.message(FeedApiMessage.DELETE_FEED_SUCCESS.getMessage())
			.success();
	}
}
