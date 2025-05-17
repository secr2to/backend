package com.emelmujiro.secreto.feed.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CreateFeedResponseDto;
import com.emelmujiro.secreto.feed.entity.Feed;
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
		feedFactory.addImages(feed, createFeedRequest.getImages());
		feedFactory.addTagUsers(feed, createFeedRequest.getTags());

		return CreateFeedResponseDto.from(feed);
	}
}
