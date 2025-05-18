package com.emelmujiro.secreto.feed.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.emelmujiro.secreto.feed.dto.response.CreateFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetCommunityFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetCommunityResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetIngameFeedsResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetRepliesResponseDto;
import com.emelmujiro.secreto.feed.dto.response.IngameFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.WriteReplyResponseDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedHeart;
import com.emelmujiro.secreto.feed.entity.FeedImage;
import com.emelmujiro.secreto.feed.entity.FeedReply;
import com.emelmujiro.secreto.feed.entity.FeedReplyHeart;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedHeartRepository;
import com.emelmujiro.secreto.feed.repository.FeedImageRepository;
import com.emelmujiro.secreto.feed.repository.FeedQueryRepository;
import com.emelmujiro.secreto.feed.repository.FeedReplyHeartRepository;
import com.emelmujiro.secreto.feed.repository.FeedReplyQueryRepository;
import com.emelmujiro.secreto.feed.repository.FeedReplyRepository;
import com.emelmujiro.secreto.feed.repository.FeedRepository;
import com.emelmujiro.secreto.feed.service.factory.FeedFactory;
import com.emelmujiro.secreto.feed.service.factory.FeedReplyFactory;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final FeedRepository feedRepository;
	private final FeedFactory feedFactory;
	private final FeedReplyFactory feedReplyFactory;
	private final FeedReplyRepository feedReplyRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final FeedReplyQueryRepository feedReplyQueryRepository;
	private final FeedHeartRepository feedHeartRepository;
	private final FeedReplyHeartRepository feedReplyHeartRepository;
	private final FeedImageRepository feedImageRepository;

	public GetCommunityResponseDto getCommunity(GetCommunityRequestDto dto) {
		return feedQueryRepository.findCommunityFeeds(dto);
	}

	public GetCommunityFeedResponseDto getCommunityFeed(GetCommunityFeedRequestDto dto) {
		Feed feed = feedRepository.findByIdWithAuthorAndImages(dto.getFeedId())
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
		List<User> heartUsers = feedHeartRepository.findByFeedIdWithUser(dto.getFeedId())
			.stream()
			.map(FeedHeart::getUser)
			.toList();
		return GetCommunityFeedResponseDto.from(feed, heartUsers, dto.getUserId());
	}

	public GetIngameFeedsResponseDto getIngameFeeds(GetIngameFeedsRequestDto dto) {
		GetIngameFeedsResponseDto response = feedQueryRepository.findIngameFeedsWithoutHeartsAndImages(dto);
		List<IngameFeedResponseDto> content = response.getContent();
		List<Long> feedIds = content.stream()
			.map(IngameFeedResponseDto::getFeedId)
			.toList();

		Map<Long, List<User>> heartUsersMap = feedHeartRepository.findAllByFeedIdInWithUser(feedIds)
			.stream()
			.collect(Collectors.groupingBy(
				heart -> heart.getFeed().getId(),
				Collectors.mapping(FeedHeart::getUser, Collectors.toList())
			));
		Map<Long, List<FeedImage>> imagesMap = feedImageRepository.findAllByFeedIdIn(feedIds)
			.stream()
			.collect(Collectors.groupingBy(
				image -> image.getFeed().getId()
			));

		content.forEach(feedDto -> {
			feedDto.applyImages(imagesMap.getOrDefault(feedDto.getFeedId(), List.of()));
			feedDto.applyHearts(heartUsersMap.getOrDefault(feedDto.getFeedId(), List.of()), dto.getUserId());
		});
		return response;
	}

	public GetRepliesResponseDto getReplies(GetRepliesRequestDto dto) {
		return feedReplyQueryRepository.getReplies(dto);
	}

	@Transactional
	public CreateFeedResponseDto create(CreateFeedRequestDto dto) {
		Long roomId = dto.getRoomId();
		Room room = roomId != null
			? roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("invalid room id"))
			: null;

		Long authorId = dto.getAuthorId();
		User author = userRepository.findById(authorId)
			.orElseThrow(() -> new IllegalArgumentException("invalid user."));

		Feed feed = feedFactory.createFeed(room, author, dto);
		feedFactory.syncImages(feed, dto.getImages());
		feedFactory.syncTags(feed, dto.getTags());

		return CreateFeedResponseDto.from(feed);
	}

	@Transactional
	public Map<String, Object> update(UpdateFeedRequestDto dto) {
		Feed feed = getFeed(dto.getFeedId(), dto.getRoomId(), dto.getAuthorId());
		feed.update(dto.getTitle(), dto.getContent());
		feedFactory.syncImages(feed, dto.getImages());
		feedFactory.syncTags(feed, dto.getTags());
		return Map.of("success", true);
	}

	@Transactional
	public Map<String, Object> delete(DeleteFeedRequestDto dto) {
		Feed feed = getFeed(dto.getFeedId(), dto.getRoomId(), dto.getAuthorId());
		return Map.of("success", feed.delete());
	}

	@Transactional
	public Map<String, Object> heart(HeartRequestDto dto) {
		/* TODO: 방에 속한 유저인지 검사 */
		Feed feed = getFeed(dto.getFeedId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("invalid user."));

		boolean success = false;
		if (feedHeartRepository.findByFeedIdAndUserId(feed.getId(), user.getId()).isEmpty()) {
			FeedHeart heart = feedHeartRepository.save(new FeedHeart(feed, user));
			feed.addHeart(heart);
			success = true;
		}
		return Map.of("success", success);
	}

	@Transactional
	public Map<String, Object> unheart(HeartRequestDto dto) {
		/* TODO: 방에 속한 유저인지 검사 */
		Feed feed = getFeed(dto.getFeedId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("invalid user."));

		FeedHeart heart = feedHeartRepository.findByFeedIdAndUserId(feed.getId(), user.getId())
			.orElse(null);

		boolean success = false;
		if (heart != null) {
			feed.removeHeart(heart);
			feedHeartRepository.delete(heart);
			success = true;
		}
		return Map.of("success", success);
	}

	@Transactional
	public WriteReplyResponseDto writeReply(WriteReplyRequestDto dto) {
		/* TODO: 방에 속한 유저인지 검사 */
		Feed feed = getFeed(dto.getFeedId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("user not found"));
		FeedReply reply = feedReplyFactory.createReply(feed, user, dto);
		feed.addReply(reply);
		return WriteReplyResponseDto.from(reply);
	}

	@Transactional
	public Map<String, Object> updateReply(UpdateReplyRequestDto dto) {
		FeedReply reply = getFeedReply(dto.getReplyId(), dto.getReplierId());
		reply.updateContent(dto.getComment());
		return Map.of("success", true);
	}

	@Transactional
	public Map<String, Object> deleteReply(DeleteReplyRequestDto dto) {
		FeedReply reply = getFeedReply(dto.getReplyId(), dto.getReplierId());
		return Map.of("success", reply.getFeed().removeReply(reply));
	}

	@Transactional
	public Map<String, Object> replyHeart(ReplyHeartRequestDto dto) {
		FeedReply reply = getFeedReply(dto.getReplyId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("invalid user."));

		boolean success = false;
		if (feedReplyHeartRepository.findByReplyIdAndUserId(dto.getReplyId(), user.getId()).isEmpty()) {
			FeedReplyHeart heart = feedReplyHeartRepository.save(new FeedReplyHeart(reply, user));
			reply.addHeart(heart);
			success = true;
		}
		return Map.of("success", success);
	}

	@Transactional
	public Map<String, Object> replyUnheart(ReplyHeartRequestDto dto) {
		FeedReply reply = getFeedReply(dto.getReplyId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("invalid user."));

		FeedReplyHeart heart = feedReplyHeartRepository.findByReplyIdAndUserId(reply.getId(), user.getId())
			.orElse(null);

		boolean success = false;
		if (heart != null) {
			reply.removeHeart(heart);
			feedReplyHeartRepository.delete(heart);
			success = true;
		}
		return Map.of("success", success);
	}

	public Feed getFeed(Long feedId) {
		return feedRepository.findActiveById(feedId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
	}

	public Feed getFeed(Long feedId, Long authorId) {
		return feedRepository.findByIdAndAuthorId(feedId, authorId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND_OR_FORBIDDEN));
	}

	public Feed getFeed(Long feedId, Long roomId, Long authorId) {
		/**
		 * TODO: Room에 속한 유저인지 체크
		 */
		return getFeed(feedId, authorId);
	}

	public FeedReply getFeedReply(Long replyId) {
		return feedReplyRepository.findActiveById(replyId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.REPLY_NOT_FOUND_OR_FORBIDDEN));
	}

	public FeedReply getFeedReply(Long replyId, Long replierId) {
		return feedReplyRepository.findByIdAndReplierId(replyId, replierId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.REPLY_NOT_FOUND_OR_FORBIDDEN));
	}
}
