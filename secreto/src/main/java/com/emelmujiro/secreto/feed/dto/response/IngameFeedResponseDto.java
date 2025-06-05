package com.emelmujiro.secreto.feed.dto.response;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.emelmujiro.secreto.feed.entity.FeedImage;
import com.emelmujiro.secreto.feed.message.FeedMessage;
import com.emelmujiro.secreto.room.dto.response.RoomUserProfileResponseDto;
import com.emelmujiro.secreto.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class IngameFeedResponseDto {

	private final Long feedId;
	private final String title;
	private final String content;
	private final RoomUserProfileResponseDto author;
	private final int replyCount;
	private final LocalDateTime createDate;
	private int heartCount;
	private boolean heart;
	private String heartMessage;
	private int imageCount;
	private List<FeedImageResponseDto> images;

	@QueryProjection
	public IngameFeedResponseDto(Long feedId, String title, String content, RoomUserProfileResponseDto author,
		int replyCount, LocalDateTime createDate) {
		this.feedId = feedId;
		this.title = title;
		this.content = content;
		this.author = author;
		this.replyCount = replyCount;
		this.createDate = createDate;
	}

	public void applyImages(List<FeedImage> images) {
		this.imageCount = images.size();
		this.images = images.stream()
			.map(FeedImageResponseDto::from)
			.sorted(Comparator.comparingInt(FeedImageResponseDto::getOrder))
			.toList();
	}

	public void applyHearts(List<User> heartUsers, Long userId) {
		this.heartCount = heartUsers.size();
		this.heart = heartUsers
			.stream()
			.anyMatch(heart -> heart.getId().equals(userId));
		if (heartCount == 0) {
			this.heartMessage = "";
		} else if (heartCount == 1) {
			this.heartMessage = String.format(FeedMessage.HEART_MESSAGE_ONE.getMessage(), heartUsers.get(0).getSearchId());
		} else {
			this.heartMessage = String.format(FeedMessage.HEART_MESSAGE.getMessage(),
				heartUsers.get(0).getSearchId(),
				heartUsers.size() - 1);
		}
	}
}
