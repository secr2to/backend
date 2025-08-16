package com.emelmujiro.secreto.feed.service.factory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.FeedImageRequestDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedImage;
import com.emelmujiro.secreto.feed.entity.FeedTagUser;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedImageRepository;
import com.emelmujiro.secreto.global.service.S3DirectoryName;
import com.emelmujiro.secreto.global.service.S3Service;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedFactory {

	private final S3Service s3Service;
	private final FeedImageRepository feedImageRepository;

	public Feed createFeed(Room room, User author, CreateFeedRequestDto createFeedRequest) {
		return Feed.builder()
			.title(createFeedRequest.getTitle())
			.content(createFeedRequest.getContent())
			.room(room)
			.author(author)
			.build();
	}

	public void syncImages(Feed feed, Long userId, List<MultipartFile> feedImageRequests) {
		if (feedImageRequests == null || feedImageRequests.isEmpty()) {
			throw new FeedException(FeedErrorCode.IMAGE_REQUIRED);
		}
		feed.removeAllFeedImages();
		int order = 0;
		for (MultipartFile file : feedImageRequests) {
			String key;
			try {
				key = s3Service.uploadImage(file, String.valueOf(userId), S3DirectoryName.FEED_IMAGE.name());
			} catch (IOException e) {
				throw new FeedException(FeedErrorCode.FEED_IMAGE_ERROR);
			}
			feed.addFeedImage(new FeedImage(key, order++));
		}
	}

	public void updateImages(Feed feed, Long userId, List<FeedImageRequestDto> feedImageRequests) {
		if (feedImageRequests == null || feedImageRequests.isEmpty()) {
			throw new FeedException(FeedErrorCode.IMAGE_REQUIRED);
		}
		feed.removeAllFeedImages();

		feedImageRequests.sort(Comparator.comparingInt(FeedImageRequestDto::getOrder));

		int order = 0;
		for (FeedImageRequestDto dto : feedImageRequests) {
			if (dto.getImageId() != null) {
				FeedImage feedImage = feedImageRepository.findById(dto.getImageId())
					.orElseThrow(() -> new FeedException(FeedErrorCode.FEED_IMAGE_ERROR));
				feedImage.setOrder(order++);
				feed.addFeedImage(feedImage);
				continue;
			}

			String key;
			try {
				key = s3Service.uploadImage(dto.getImage(), String.valueOf(userId), S3DirectoryName.FEED_IMAGE.name());
			} catch (IOException e) {
				throw new FeedException(FeedErrorCode.FEED_IMAGE_ERROR);
			}
			feed.addFeedImage(new FeedImage(key, order++));
		}
	}

	public void syncTags(Feed feed, List<User> tagUsers) {
		feed.removeAllTagUsers();
		tagUsers
			.forEach(user -> feed.addTagUser(new FeedTagUser(feed, user)));
	}
}
