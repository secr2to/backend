package com.emelmujiro.secreto.feed.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emelmujiro.secreto.feed.dto.request.DeleteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetRepliesRequestDto;
import com.emelmujiro.secreto.feed.dto.request.ReplyHeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.WriteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.response.GetRepliesResponseDto;
import com.emelmujiro.secreto.feed.dto.response.WriteReplyResponseDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedReply;
import com.emelmujiro.secreto.feed.entity.FeedReplyHeart;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedReplyHeartRepository;
import com.emelmujiro.secreto.feed.repository.FeedReplyQueryRepository;
import com.emelmujiro.secreto.feed.repository.FeedReplyRepository;
import com.emelmujiro.secreto.feed.service.FeedReplyService;
import com.emelmujiro.secreto.global.dto.response.SuccessResponseDto;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.error.UserErrorCode;
import com.emelmujiro.secreto.user.exception.UserException;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedReplyServiceImpl implements FeedReplyService {

	private final FeedServiceImpl feedService;
	private final UserRepository userRepository;
	private final FeedReplyRepository feedReplyRepository;
	private final FeedReplyQueryRepository feedReplyQueryRepository;
	private final FeedReplyHeartRepository feedReplyHeartRepository;

	public GetRepliesResponseDto getReplies(GetRepliesRequestDto dto) {
		return feedReplyQueryRepository.getReplies(dto);
	}

	@Transactional
	public WriteReplyResponseDto writeReply(WriteReplyRequestDto dto) {
		/* TODO: 방에 속한 유저인지 검사 */
		Feed feed = feedService.getFeed(dto.getFeedId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
		User mentionedUser = dto.getMentionUserId() != null
			? userRepository.findById(dto.getMentionUserId())
			.orElseThrow(() -> new FeedException(FeedErrorCode.MENTIONED_USER_NOT_FOUND))
			: null;

		validateNotSelfMention(user, mentionedUser);

		FeedReply parentReply = getRootReply(dto.getParentReplyId());
		FeedReply reply = FeedReply.builder()
			.feed(feed)
			.comment(dto.getComment())
			.parent(parentReply)
			.replier(user)
			.mentionedUser(mentionedUser)
			.build();
		FeedReply savedReply = feedReplyRepository.save(reply);

		feed.addReply(reply);
		return WriteReplyResponseDto.from(savedReply);
	}

	private FeedReply getRootReply(Long replyId) {
		if (replyId == null) return null;

		FeedReply rootReply = getReply(replyId);
		if (rootReply != null && rootReply.isNestedReplyYn()) {
			rootReply = rootReply.getParent();
		}
		return rootReply;
	}

	private void validateNotSelfMention(User user, User mentionedUser) {
		if (user.equals(mentionedUser)) {
			throw new FeedException(FeedErrorCode.SELF_MENTION_NOT_ALLOW);
		}
	}

	@Transactional
	public SuccessResponseDto updateReply(UpdateReplyRequestDto dto) {
		FeedReply reply = getReply(dto.getReplyId(), dto.getReplierId());
		reply.updateContent(dto.getComment());
		return SuccessResponseDto.ofSuccess();
	}

	@Transactional
	public SuccessResponseDto deleteReply(DeleteReplyRequestDto dto) {
		FeedReply reply = getReply(dto.getReplyId(), dto.getReplierId());
		return SuccessResponseDto.of(reply.getFeed().removeReply(reply));
	}

	@Transactional
	public SuccessResponseDto replyHeart(ReplyHeartRequestDto dto) {
		FeedReply reply = getReply(dto.getReplyId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		boolean success = false;
		if (feedReplyHeartRepository.findByReplyIdAndUserId(dto.getReplyId(), user.getId()).isEmpty()) {
			FeedReplyHeart heart = feedReplyHeartRepository.save(new FeedReplyHeart(reply, user));
			reply.addHeart(heart);
			success = true;
		}
		return SuccessResponseDto.of(success);
	}

	@Transactional
	public SuccessResponseDto replyUnheart(ReplyHeartRequestDto dto) {
		FeedReply reply = getReply(dto.getReplyId());
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		FeedReplyHeart heart = feedReplyHeartRepository.findByReplyIdAndUserId(reply.getId(), user.getId())
			.orElse(null);

		boolean success = false;
		if (heart != null) {
			reply.removeHeart(heart);
			feedReplyHeartRepository.delete(heart);
			success = true;
		}
		return SuccessResponseDto.of(success);
	}

	private FeedReply getReply(Long replyId) {
		return feedReplyRepository.findActiveById(replyId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.REPLY_NOT_FOUND_OR_FORBIDDEN));
	}

	private FeedReply getReply(Long replyId, Long replierId) {
		return feedReplyRepository.findByIdAndReplierId(replyId, replierId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.REPLY_NOT_FOUND_OR_FORBIDDEN));
	}
}
