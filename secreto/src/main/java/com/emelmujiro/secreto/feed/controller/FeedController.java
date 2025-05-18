package com.emelmujiro.secreto.feed.controller;

import static java.lang.String.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.DeleteFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.DeleteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetRepliesRequestDto;
import com.emelmujiro.secreto.feed.dto.request.HeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.ReplyHeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.WriteReplyRequestDto;
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
	public ResponseEntity<?> getCommunityFeed(@PathVariable("feedId") Long feedId, @LoginUser Long userId) {
		GetCommunityFeedRequestDto getCommunityFeedRequest = new GetCommunityFeedRequestDto(feedId, userId);
		return ApiResponse.builder()
			.data(feedService.getCommunityFeed(getCommunityFeedRequest))
			.message(format(FeedApiMessage.GET_COMMUNITY_FEED_SUCCESS.getMessage(), feedId))
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
	public ResponseEntity<?> deleteFeed(
		@PathVariable("feedId") Long feedId,
		@PathVariable(value = "roomId", required = false) Long roomId,
		@LoginUser Long authorId
	) {
		DeleteFeedRequestDto deleteFeedRequest = new DeleteFeedRequestDto(feedId, roomId, authorId);
		return ApiResponse.builder()
			.data(feedService.delete(deleteFeedRequest))
			.message(FeedApiMessage.DELETE_FEED_SUCCESS.getMessage())
			.success();
	}

	@PostMapping({"/community/{feedId}/heart", "/rooms/{roomId}/feeds/{feedId}/heart"})
	public ResponseEntity<?> heart(
		@PathVariable("feedId") Long feedId,
		@PathVariable(value = "roomId", required = false) Long roomId,
		@LoginUser Long userId
	) {
		HeartRequestDto heartRequest = new HeartRequestDto(feedId, roomId, userId);
		return ApiResponse.builder()
			.data(feedService.heart(heartRequest))
			.message(FeedApiMessage.HEART_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping({"/community/{feedId}/heart", "/rooms/{roomId}/feeds/{feedId}/heart"})
	public ResponseEntity<?> unheart(
		@PathVariable("feedId") Long feedId,
		@PathVariable(value = "roomId", required = false) Long roomId,
		@LoginUser Long userId
	) {
		HeartRequestDto heartRequest = new HeartRequestDto(feedId, roomId, userId);
		return ApiResponse.builder()
			.data(feedService.unheart(heartRequest))
			.message(FeedApiMessage.UNHEART_SUCCESS.getMessage())
			.success();
	}

	@GetMapping({"/community/{feedId}/replies", "/rooms/{roomId}/feeds/{feedId}/replies"})
	public ResponseEntity<?> getReplies(
		@ModelAttribute GetRepliesRequestDto getRepliesRequest,
		@PathVariable("feedId") Long feedId,
		@PathVariable(value = "roomId", required = false) Long roomId,
		@LoginUser Long userId) {
		getRepliesRequest.setFeedId(feedId);
		getRepliesRequest.setRoomId(roomId);
		getRepliesRequest.setUserId(userId);
		Long replyId = getRepliesRequest.getReplyId();

		String message = replyId == null
			? format(FeedApiMessage.GET_REPLIES_SUCCESS.getMessage(), feedId)
			: format(FeedApiMessage.GET_NESTED_REPLIES_SUCCESS.getMessage(), feedId, replyId);

		return ApiResponse.builder()
			.data(feedService.getReplies(getRepliesRequest))
			.message(message)
			.success();
	}

	@PostMapping("/replies")
	public ResponseEntity<?> writeReply(@RequestBody WriteReplyRequestDto writeReplyRequest) {
		return ApiResponse.builder()
			.data(feedService.writeReply(writeReplyRequest))
			.message(FeedApiMessage.WRITE_REPLY_SUCCESS.getMessage())
			.success();
	}

	@PutMapping("/replies/{replyId}")
	public ResponseEntity<?> updateReply(@RequestBody UpdateReplyRequestDto updateReplyRequest) {
		return ApiResponse.builder()
			.data(feedService.updateReply(updateReplyRequest))
			.message(FeedApiMessage.UPDATE_REPLY_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping("/replies/{replyId}")
	public ResponseEntity<?> deleteReply(@PathVariable("replyId") Long replyId, @LoginUser Long userId) {
		DeleteReplyRequestDto deleteReplyRequest = new DeleteReplyRequestDto(replyId, userId);
		return ApiResponse.builder()
			.data(feedService.deleteReply(deleteReplyRequest))
			.message(FeedApiMessage.DELETE_REPLY_SUCCESS.getMessage())
			.success();
	}

	@PostMapping("/replies/{replyId}/heart")
	public ResponseEntity<?> replyHeart(
		@PathVariable("replyId") Long replyId,
		@LoginUser Long userId
	) {
		ReplyHeartRequestDto heartRequest = new ReplyHeartRequestDto(replyId, userId);
		return ApiResponse.builder()
			.data(feedService.replyHeart(heartRequest))
			.message(FeedApiMessage.HEART_SUCCESS.getMessage())
			.success();
	}

	@DeleteMapping("/replies/{replyId}/heart")
	public ResponseEntity<?> replyUnheart(
		@PathVariable("replyId") Long replyId,
		@LoginUser Long userId
	) {
		ReplyHeartRequestDto heartRequest = new ReplyHeartRequestDto(replyId, userId);
		return ApiResponse.builder()
			.data(feedService.replyUnheart(heartRequest))
			.message(FeedApiMessage.UNHEART_SUCCESS.getMessage())
			.success();
	}
}
