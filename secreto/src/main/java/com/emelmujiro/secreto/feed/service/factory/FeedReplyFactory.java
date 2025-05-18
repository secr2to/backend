package com.emelmujiro.secreto.feed.service.factory;

import org.springframework.stereotype.Component;

import com.emelmujiro.secreto.feed.dto.request.WriteReplyRequestDto;
import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedReply;
import com.emelmujiro.secreto.feed.error.FeedErrorCode;
import com.emelmujiro.secreto.feed.exception.FeedException;
import com.emelmujiro.secreto.feed.repository.FeedReplyRepository;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedReplyFactory {

	private final FeedReplyRepository feedReplyRepository;
	private final UserRepository userRepository;

	public FeedReply createReply(Feed feed, User user, WriteReplyRequestDto writeReplyRequest) {
		FeedReply parentReply = getReply(writeReplyRequest.getParentReplyId());

		/* TODO: 댓글 태그 기능 처리 */
		if (parentReply != null && parentReply.isNestedReplyYn()) {
			parentReply = parentReply.getParent();
		}
		User mentionedUser = getUser(writeReplyRequest.getMentionUserId());

		if (user == mentionedUser) {
			throw new FeedException(FeedErrorCode.SELF_MENTION_NOT_ALLOW);
		}
		return feedReplyRepository.save(
			FeedReply.builder()
				.feed(feed)
				.comment(writeReplyRequest.getComment())
				.parent(parentReply)
				.replier(user)
				.mentionedUser(mentionedUser)
				.build()
		);
	}

	private FeedReply getReply(Long replyId) {
		if (replyId == null) return null;
		return feedReplyRepository.findActiveById(replyId)
			.orElseThrow(() -> new FeedException(FeedErrorCode.PARENT_REPLY_NOT_FOUND));
	}

	/** 임시 **/
	private User getUser(Long userId) {
		if (userId == null) return null;
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("user not found"));/* TODO: 변경 */
	}
}
