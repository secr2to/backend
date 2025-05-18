package com.emelmujiro.secreto.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedType;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.message.FeedApiMessage;
import com.emelmujiro.secreto.user.dto.response.UserProfileResponseDto;
import com.emelmujiro.secreto.user.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GetCommunityFeedResponseDto {

	private Long feedId;
	private String title;
	private String content;
	private FeedType feedType;
	private UserProfileResponseDto author;
	private int heartCount;
	private boolean heart;
	private String heartMessage;
	private LocalDateTime createTime;
	private int imageCount;
	private List<FeedImageResponseDto> images;
	private int replyCount;

	public static GetCommunityFeedResponseDto from(Feed feed, List<User> heartUsers, Long userId) {
		if (feed.getFeedType() != FeedType.COMMUNITY)
			throw new FeedException(FeedErrorCode.UNMATCHED_FEED_TYPE);

		int heartCount = heartUsers.size();
		List<FeedImageResponseDto> imageResponseDtos = feed.getImages()
			.stream()
			.map(FeedImageResponseDto::from)
			.toList();

		String heartMessage;
		if (heartCount == 0) {
			heartMessage = "";
		} else if (heartCount == 1) {
			heartMessage = String.format(FeedApiMessage.HEART_MESSAGE_ONE.getMessage(), heartUsers.get(0).getSearchId());
		} else {
			heartMessage = String.format(FeedApiMessage.HEART_MESSAGE.getMessage(),
				heartUsers.get(0).getSearchId(),
				heartUsers.size() - 1);
		}
		return GetCommunityFeedResponseDto.builder()
			.feedId(feed.getId())
			.title(feed.getTitle())
			.content(feed.getContent())
			.feedType(feed.getFeedType())
			.author(UserProfileResponseDto.from(feed.getAuthor()))
			.heartCount(heartCount)
			.heart(heartUsers.stream()
				.anyMatch(user -> user.getId().equals(userId)))
			.heartMessage(heartMessage)
			.createTime(feed.getCreateDate())
			.imageCount(imageResponseDtos.size())
			.images(imageResponseDtos)
			.replyCount(feed.getReplyCount())
			.build();
	}
}
