package com.emelmujiro.secreto.feed.service.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.FeedImageRequestDto;
import com.emelmujiro.secreto.feed.dto.request.FeedTagRequestDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedImage;
import com.emelmujiro.secreto.feed.entity.FeedTagUser;
import com.emelmujiro.secreto.feed.repository.FeedImageRepository;
import com.emelmujiro.secreto.feed.repository.FeedRepository;
import com.emelmujiro.secreto.feed.repository.FeedTagUserRepository;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedFactory {

	private final FeedRepository feedRepository;
	private final FeedImageRepository feedImageRepository;
	private final FeedTagUserRepository feedTagUserRepository;
	private final UserRepository userRepository;

	public Feed createFeed(CreateFeedRequestDto createFeedRequest, Room room, User author) {
		return feedRepository.save(
			Feed.builder()
				.title(createFeedRequest.getTitle())
				.content(createFeedRequest.getContent())
				.room(room)
				.author(author)
				.build()
		);
	}

	public List<FeedImage> addImages(Feed feed, List<FeedImageRequestDto> feedImageRequestDtos) {
		return feedImageRepository.saveAll(
			feedImageRequestDtos
				.stream()
				.map(image -> new FeedImage(feed, image.getImageUrl()))
				.toList()
		);
	}

	public List<FeedTagUser> addTagUsers(Feed feed, List<FeedTagRequestDto> feedTagRequestDtos) {
		List<Long> tagUserIds = feedTagRequestDtos
			.stream()
			.map(FeedTagRequestDto::getUserId)
			.toList();

		Map<Long, User> userMap = userRepository.findAllById(tagUserIds)
			.stream()
			.collect(Collectors.toMap(User::getId, Function.identity()));

		return feedTagUserRepository.saveAll(
			feedTagRequestDtos
				.stream()
				.map(tagUser ->
					new FeedTagUser(feed, userMap.get(tagUser.getUserId()))
				)
				.filter(tagUser -> tagUser.getUser() != null)
				.toList()
		);
	}
}
