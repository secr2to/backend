package com.emelmujiro.secreto.feed.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.DeleteFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.FeedTagRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetCommunityRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetIngameFeedsRequestDto;
import com.emelmujiro.secreto.feed.dto.request.HeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CreateFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetCommunityFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetCommunityResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetIngameFeedsResponseDto;
import com.emelmujiro.secreto.feed.dto.response.IngameFeedResponseDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedHeart;
import com.emelmujiro.secreto.feed.entity.FeedImage;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedHeartRepository;
import com.emelmujiro.secreto.feed.repository.FeedImageRepository;
import com.emelmujiro.secreto.feed.repository.FeedQueryRepository;
import com.emelmujiro.secreto.feed.repository.FeedRepository;
import com.emelmujiro.secreto.feed.service.FeedService;
import com.emelmujiro.secreto.feed.service.factory.FeedFactory;
import com.emelmujiro.secreto.global.dto.response.SuccessResponseDto;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.error.UserErrorCode;
import com.emelmujiro.secreto.user.exception.UserException;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

	private final FeedFactory feedFactory;
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final RoomUserRepository roomUserRepository;
	private final FeedRepository feedRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final FeedHeartRepository feedHeartRepository;
	private final FeedImageRepository feedImageRepository;

	@Override
	public GetCommunityResponseDto getCommunity(GetCommunityRequestDto dto) {
		return feedQueryRepository.findCommunityFeeds(dto);
	}

	@Override
	public GetCommunityFeedResponseDto getCommunityFeed(GetCommunityFeedRequestDto dto) {
		Feed feed = feedRepository.findByIdWithAuthorAndImages(dto.getFeedId())
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
		List<User> heartUsers = feedHeartRepository.findByFeedIdWithUser(dto.getFeedId())
			.stream()
			.map(FeedHeart::getUser)
			.toList();
		return GetCommunityFeedResponseDto.from(feed, heartUsers, dto.getUserId());
	}

	@Override
	public GetIngameFeedsResponseDto getIngameFeeds(GetIngameFeedsRequestDto dto) {
		if (roomUserRepository.findByUserIdAndRoomId(dto.getUserId(), dto.getRoomId()).isEmpty()) {
			throw new FeedException(FeedErrorCode.FORBIDDEN_FEED_ROOM_ACCESS);
		}
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

	@Override
	@Transactional
	public CreateFeedResponseDto create(CreateFeedRequestDto dto) {
		Long roomId = dto.getRoomId();
		Room room = roomId != null
			? roomRepository.findById(roomId).orElseThrow(() -> new FeedException(FeedErrorCode.FEED_ROOM_NOT_FOUND))
			: null;

		Long authorId = dto.getAuthorId();
		User author = userRepository.findById(authorId)
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		List<User> tagUsers = getTagUsers(dto.getTags());

		Feed feed = feedFactory.createFeed(room, author, dto);
		feedFactory.syncImages(feed, dto.getImages());
		feedFactory.syncTags(feed, tagUsers);
		Feed savedFeed = feedRepository.save(feed);
		return CreateFeedResponseDto.from(savedFeed);
	}

	@Override
	@Transactional
	public SuccessResponseDto update(UpdateFeedRequestDto dto) {
		Feed feed = getFeed(dto.getFeedId(), dto.getAuthorId());
		List<User> tagUsers = getTagUsers(dto.getTags());

		feed.update(dto.getTitle(), dto.getContent());
		feedFactory.syncImages(feed, dto.getImages());
		feedFactory.syncTags(feed, tagUsers);
		return SuccessResponseDto.ofSuccess();
	}

	private List<User> getTagUsers(List<FeedTagRequestDto> tags) {
		if (tags == null)
			return List.of();

		List<Long> tagUserIds = tags
			.stream()
			.map(FeedTagRequestDto::getUserId)
			.toList();
		return userRepository.findAllById(tagUserIds);
	}

	@Override
	@Transactional
	public SuccessResponseDto delete(DeleteFeedRequestDto dto) {
		Feed feed = getFeed(dto.getFeedId(), dto.getAuthorId());
		return SuccessResponseDto.of(feed.delete());
	}

	@Override
	@Transactional
	public SuccessResponseDto heart(HeartRequestDto dto) {
		Feed feed = getFeed(dto.getFeedId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		boolean success = false;
		if (feedHeartRepository.findByFeedIdAndUserId(feed.getId(), user.getId()).isEmpty()) {
			FeedHeart heart = feedHeartRepository.save(new FeedHeart(feed, user));
			feed.addHeart(heart);
			success = true;
		}
		return SuccessResponseDto.of(success);
	}

	@Override
	@Transactional
	public SuccessResponseDto unheart(HeartRequestDto dto) {
		Feed feed = getFeed(dto.getFeedId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		FeedHeart heart = feedHeartRepository.findByFeedIdAndUserId(feed.getId(), user.getId())
			.orElse(null);

		boolean success = false;
		if (heart != null) {
			feed.removeHeart(heart);
			feedHeartRepository.delete(heart);
			success = true;
		}
		return SuccessResponseDto.of(success);
	}

	Feed getFeed(Long feedId) {
		return feedRepository.findActiveById(feedId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
	}

	Feed getFeed(Long feedId, Long authorId) {
		return feedRepository.findByIdAndAuthorId(feedId, authorId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND_OR_FORBIDDEN));
	}
}
