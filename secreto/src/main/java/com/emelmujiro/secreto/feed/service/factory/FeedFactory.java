package com.emelmujiro.secreto.feed.service.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.FeedImageRequestDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedImage;
import com.emelmujiro.secreto.feed.entity.FeedTagUser;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;

@Component
public class FeedFactory {

	public Feed createFeed(Room room, User author, CreateFeedRequestDto createFeedRequest) {
		return Feed.builder()
			.title(createFeedRequest.getTitle())
			.content(createFeedRequest.getContent())
			.room(room)
			.author(author)
			.build();
	}

	public void syncImages(Feed feed, List<FeedImageRequestDto> feedImageRequests) {
		if (feedImageRequests == null || feedImageRequests.isEmpty()) {
			throw new FeedException(FeedErrorCode.IMAGE_REQUIRED);
		}
		feed.removeAllFeedImages();
		int order = 0;
		for (FeedImageRequestDto dto : feedImageRequests) {
			feed.addFeedImage(new FeedImage(dto.getImageUrl(), order++));
		}
	}

	public void syncTags(Feed feed, List<User> tagUsers) {
		feed.removeAllTagUsers();
		tagUsers
			.forEach(user -> feed.addTagUser(new FeedTagUser(feed, user)));
	}
}
