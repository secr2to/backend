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

			for (int i = 0; i < 3; ++i) {
				CreateFeedRequestDto dto = new CreateFeedRequestDto();
				dto.setTitle("title");
				dto.setContent("content");
				dto.setAuthorId(user.getId());
				dto.setImages(
					new ArrayList<>(List.of(new FeedImageRequestDto("image.url")))
				);
				CreateFeedResponseDto feedResponse = feedService.create(dto);

				Long feedId = feedResponse.getFeedId();
				for (int j = 0; j < 100; ++j) {
					WriteReplyRequestDto replyDto = new WriteReplyRequestDto();
					replyDto.setFeedId(feedId);
					replyDto.setComment("reply" + i + "..." + j);
					replyDto.setUserId(user.getId());
					WriteReplyResponseDto replyResponse = feedService.writeReply(replyDto);
					Long replyId = replyResponse.getReplyId();
					if (j > 1)
						continue;
					for (int k = 0; k < 100; ++k) {
						WriteReplyRequestDto nestedReplyDto = new WriteReplyRequestDto();
						nestedReplyDto.setFeedId(feedId);
						nestedReplyDto.setComment("nested reply" + i + "..." + j + "..." + k);
						nestedReplyDto.setUserId(user.getId());
						nestedReplyDto.setParentReplyId(replyId);
						feedService.writeReply(nestedReplyDto);
					}
				}
			}
		}
	}
}
