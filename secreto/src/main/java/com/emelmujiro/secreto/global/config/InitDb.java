package com.emelmujiro.secreto.global.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.FeedImageRequestDto;
import com.emelmujiro.secreto.feed.dto.request.WriteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CreateFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.WriteReplyResponseDto;
import com.emelmujiro.secreto.feed.service.FeedService;
import com.emelmujiro.secreto.room.dto.request.CreateRoomReqDto;
import com.emelmujiro.secreto.room.dto.response.CreateRoomResDto;
import com.emelmujiro.secreto.room.service.RoomService;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		log.info("init...");
		initService.feedInit();
		log.info("ok!");
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private final UserRepository userRepository;
		private final FeedService feedService;
		private final RoomService roomService;

		public void feedInit() {
			User user = userRepository.save(
				User.oauthUserBuilder()
					.email("test@naver.com")
					.oAuthProvider("naver")
					.profileUrl("https://avatars.githubusercontent.com/u/80024307?v=4")
					.username("sub")
					.searchId("searchId")
					.build()
			);

			CreateRoomResDto roomResponse = roomService.createRoom(CreateRoomReqDto.builder()
				.managerId(user.getId())
				.name("room1")
				.build());

			for (int i = 0; i < 3; ++i) {
				CreateFeedRequestDto dto = CreateFeedRequestDto.builder()
					.title("title")
					.content("content")
					.authorId(user.getId())
					.roomId(i == 2 ? roomResponse.getRoomId() : null)
					.images(new ArrayList<>(List.of(new FeedImageRequestDto("image.url"))))
					.build();
				CreateFeedResponseDto feedResponse = feedService.create(dto);

				Long feedId = feedResponse.getFeedId();
				for (int j = 0; j < 100; ++j) {
					WriteReplyRequestDto replyDto = WriteReplyRequestDto.builder()
						.feedId(feedId)
						.comment("reply" + i + "..." + j)
						.userId(user.getId())
						.build();
					WriteReplyResponseDto replyResponse = feedService.writeReply(replyDto);
					Long replyId = replyResponse.getReplyId();
					if (j > 1)
						continue;
					for (int k = 0; k < 100; ++k) {
						WriteReplyRequestDto nestedReplyDto = WriteReplyRequestDto.builder()
							.feedId(feedId)
							.comment("nested reply" + i + "..." + j + "..." + k)
							.userId(user.getId())
							.parentReplyId(replyId)
							.build();
						feedService.writeReply(nestedReplyDto);
					}
				}
			}
		}
	}
}
