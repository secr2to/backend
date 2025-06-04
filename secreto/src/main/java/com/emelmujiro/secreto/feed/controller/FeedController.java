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
import com.emelmujiro.secreto.feed.dto.request.DeleteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetIngameFeedsRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetRepliesRequestDto;
import com.emelmujiro.secreto.feed.dto.request.HeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.ReplyHeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.WriteReplyRequestDto;
import com.emelmujiro.secreto.feed.message.FeedMessage;
import com.emelmujiro.secreto.feed.service.FeedReplyService;
import com.emelmujiro.secreto.feed.service.FeedService;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;
	private final FeedReplyService feedReplyService;

	@GetMapping("/community")
	public ResponseEntity<?> getCommunity(@ModelAttribute GetCommunityRequestDto getFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.getCommunity(getFeedRequest))
			.message(format(FeedMessage.GET_COMMUNITY_SUCCESS.getMessage(),
				getFeedRequest.getOffset(),
				getFeedRequest.getKeyword())
			)
			.success();
	}

	@GetMapping("/community/{feedId}")
	public ResponseEntity<?> getCommunityFeed(@ModelAttribute GetCommunityFeedRequestDto getCommunityFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.getCommunityFeed(getCommunityFeedRequest))
			.message(format(FeedMessage.GET_COMMUNITY_FEED_SUCCESS.getMessage(), getCommunityFeedRequest.getFeedId()))
			.success();
	}

	@GetMapping("/rooms/{roomId}/feeds")
	public ResponseEntity<?> getIngameFeeds(@ModelAttribute GetIngameFeedsRequestDto getIngameFeedsRequest) {
		return ApiResponse.builder()
			.data(feedService.getIngameFeeds(getIngameFeedsRequest))
			.message(format(FeedMessage.GET_INGAME_FEEDS_SUCCESS.getMessage(), getIngameFeedsRequest.getRoomId()))
			.success();
	}

	@PostMapping({"/community", "/rooms/{roomId}/feeds"})
	public ResponseEntity<?> createFeed(@RequestBody CreateFeedRequestDto createFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.create(createFeedRequest))
			.message(FeedMessage.CREATE_FEED_SUCCESS.getMessage())
			.success();
	}

	@PutMapping("/feeds/{feedId}")
	public ResponseEntity<?> updateFeed(@RequestBody UpdateFeedRequestDto updateFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.update(updateFeedRequest))
			.message(FeedMessage.UPDATE_FEED_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping("/feeds/{feedId}")
	public ResponseEntity<?> deleteFeed(@ModelAttribute DeleteFeedRequestDto deleteFeedRequest) {
		return ApiResponse.builder()
			.data(feedService.delete(deleteFeedRequest))
			.message(FeedMessage.DELETE_FEED_SUCCESS.getMessage())
			.success();
	}

	@PostMapping("/feeds/{feedId}/heart")
	public ResponseEntity<?> heart(@ModelAttribute HeartRequestDto heartRequest) {
		return ApiResponse.builder()
			.data(feedService.heart(heartRequest))
			.message(FeedMessage.HEART_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping("/feeds/{feedId}/heart")
	public ResponseEntity<?> unheart(@ModelAttribute HeartRequestDto heartRequest) {
		return ApiResponse.builder()
			.data(feedService.unheart(heartRequest))
			.message(FeedMessage.UNHEART_SUCCESS.getMessage())
			.success();
	}

	@GetMapping({"/community/{feedId}/replies", "/rooms/{roomId}/feeds/{feedId}/replies"})
	public ResponseEntity<?> getReplies(@ModelAttribute GetRepliesRequestDto getRepliesRequest) {
		Long feedId = getRepliesRequest.getFeedId();
		Long replyId = getRepliesRequest.getReplyId();

		String message = replyId == null
			? format(FeedMessage.GET_REPLIES_SUCCESS.getMessage(), feedId)
			: format(FeedMessage.GET_NESTED_REPLIES_SUCCESS.getMessage(), feedId, replyId);

		return ApiResponse.builder()
			.data(feedReplyService.getReplies(getRepliesRequest))
			.message(message)
			.success();
	}

	@PostMapping("/replies")
	public ResponseEntity<?> writeReply(@RequestBody WriteReplyRequestDto writeReplyRequest) {
		return ApiResponse.builder()
			.data(feedReplyService.writeReply(writeReplyRequest))
			.message(FeedMessage.WRITE_REPLY_SUCCESS.getMessage())
			.success();
	}

	@PutMapping("/replies/{replyId}")
	public ResponseEntity<?> updateReply(@RequestBody UpdateReplyRequestDto updateReplyRequest) {
		return ApiResponse.builder()
			.data(feedReplyService.updateReply(updateReplyRequest))
			.message(FeedMessage.UPDATE_REPLY_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping("/replies/{replyId}")
	public ResponseEntity<?> deleteReply(@ModelAttribute DeleteReplyRequestDto deleteReplyRequest) {
		return ApiResponse.builder()
			.data(feedReplyService.deleteReply(deleteReplyRequest))
			.message(FeedMessage.DELETE_REPLY_SUCCESS.getMessage())
			.success();
	}

	@PostMapping("/replies/{replyId}/heart")
	public ResponseEntity<?> replyHeart(@ModelAttribute ReplyHeartRequestDto heartRequest) {
		return ApiResponse.builder()
			.data(feedReplyService.replyHeart(heartRequest))
			.message(FeedMessage.HEART_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping("/replies/{replyId}/heart")
	public ResponseEntity<?> replyUnheart(@ModelAttribute ReplyHeartRequestDto heartRequest) {
		return ApiResponse.builder()
			.data(feedReplyService.replyUnheart(heartRequest))
			.message(FeedMessage.UNHEART_SUCCESS.getMessage())
			.success();
	}
}
