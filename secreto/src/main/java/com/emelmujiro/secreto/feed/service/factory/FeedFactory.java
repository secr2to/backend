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
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedRepository;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedFactory {

	private final FeedRepository feedRepository;
	private final UserRepository userRepository;

	public Feed getFeed(Long feedId) {
		return feedRepository.findById(feedId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
	}

	public Feed getFeed(Long feedId, Long authorId) {
		return feedRepository.findByIdAndAuthorId(feedId, authorId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND_OR_FORBIDDEN));
	}

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

	public void syncImages(Feed feed, List<FeedImageRequestDto> feedImageRequestDtos) {
		feed.removeAllFeedImages();
		int order = 0;
		for (FeedImageRequestDto dto: feedImageRequestDtos) {
			feed.addFeedImage(new FeedImage(dto.getImageUrl(), order++));
		}
	}

	public void syncTags(Feed feed, List<FeedTagRequestDto> feedTagRequestDtos) {
		feed.removeAllTagUsers();
		List<Long> tagUserIds = feedTagRequestDtos
			.stream()
			.map(FeedTagRequestDto::getUserId)
			.toList();

		Map<Long, User> userMap = userRepository.findAllById(tagUserIds)
			.stream()
			.collect(Collectors.toMap(User::getId, Function.identity()));

		feedTagRequestDtos
			.forEach(tag ->
				feed.addTagUser(new FeedTagUser(feed, userMap.get(tag.getUserId())))
			);
	}
}
