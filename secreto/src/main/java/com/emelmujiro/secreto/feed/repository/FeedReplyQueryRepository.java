package com.emelmujiro.secreto.feed.repository;

import static com.emelmujiro.secreto.feed.entity.QFeedReply.*;
import static com.emelmujiro.secreto.user.entity.QUser.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.emelmujiro.secreto.feed.dto.request.GetRepliesRequestDto;
import com.emelmujiro.secreto.feed.dto.response.GetRepliesResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QFeedUserResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QReplyResponseDto;
import com.emelmujiro.secreto.feed.dto.response.ReplyResponseDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class FeedReplyQueryRepository {

	@Value("${feed.reply-paging-size}")
	private int pageSize;

	private final JPAQueryFactory query;

	public FeedReplyQueryRepository(EntityManager em) {
		this.query = new JPAQueryFactory(em);
	}

	public GetRepliesResponseDto getReplies(GetRepliesRequestDto dto) {
		List<ReplyResponseDto> content = query
			.select(new QReplyResponseDto(
				feedReply.id,
				feedReply.comment,
				feedReply.createDate,
				feedReply.nestedReplyYn,
				feedReply.nestedReplyCount,
				feedReply.heartCount,
				Expressions.asBoolean(false), /* TODO: 좋아요 여부 */
				new QFeedUserResponseDto(
					user.id,
					user.searchId,
					user.profileUrl,
					Expressions.nullExpression(),
					Expressions.nullExpression()
				)
			))
			.from(feedReply)
			.leftJoin(feedReply.replier, user)
			.where(feedReply.feed.id.eq(dto.getFeedId()),
				feedReply.deletedYn.eq(false),
				replyCondition(dto),
				nestedReplyCondition(dto))
			.orderBy(feedReply.createDate.desc())
			.offset(dto.getOffset())
			.limit(pageSize + 1)
			.fetch();

		boolean hasNext = false;
		if (content.size() > pageSize) {
			content.remove(pageSize);
			hasNext = true;
		}
		return GetRepliesResponseDto.builder()
			.content(content)
			.offset(hasNext ? dto.getOffset() + pageSize : -1)
			.hasNext(hasNext)
			.build();
	}

	private BooleanExpression replyCondition(GetRepliesRequestDto dto) {
		if (dto.getReplyId() != null) return null;
		return feedReply.nestedReplyYn.eq(false);
	}

	private BooleanExpression nestedReplyCondition(GetRepliesRequestDto dto) {
		if (dto.getReplyId() == null) return null;
		return feedReply.nestedReplyYn.eq(true)
			.and(feedReply.parent.id.eq(dto.getReplyId()));
	}
}
