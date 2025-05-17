package com.emelmujiro.secreto.feed.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CreateFeedResponseDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedRepository;
import com.emelmujiro.secreto.feed.service.factory.FeedFactory;
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
	private final FeedFactory feedFactory;
	private final FeedRepository feedRepository;

	@Transactional
	public CreateFeedResponseDto create(CreateFeedRequestDto createFeedRequest) {
		Long roomId = createFeedRequest.getRoomId();
		Room room = roomId != null
			? roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("invalid room id"))
			: null;

		Long authorId = createFeedRequest.getAuthorId();
		User author = userRepository.findById(authorId)
			.orElseThrow(() -> new IllegalArgumentException("invalid user."));

		Feed feed = feedFactory.createFeed(createFeedRequest, room, author);
		feedFactory.syncImages(feed, createFeedRequest.getImages());
		feedFactory.syncTags(feed, createFeedRequest.getTags());

		return CreateFeedResponseDto.from(feed);
	}

	@Transactional
	public Map<String, Object> update(UpdateFeedRequestDto updateFeedRequest) {
		Feed feed = feedFactory.getFeed(updateFeedRequest.getFeedId(), updateFeedRequest.getAuthorId());

		feed.update(updateFeedRequest.getTitle(), updateFeedRequest.getContent());
		feedFactory.syncImages(feed, updateFeedRequest.getImages());
		feedFactory.syncTags(feed, updateFeedRequest.getTags());

		return Map.of("success", true);
	}
}
