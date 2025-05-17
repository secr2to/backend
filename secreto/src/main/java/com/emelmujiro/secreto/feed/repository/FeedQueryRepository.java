package com.emelmujiro.secreto.feed.repository;

import static com.emelmujiro.secreto.feed.entity.QFeed.*;
import static com.emelmujiro.secreto.user.entity.QUser.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.emelmujiro.secreto.feed.dto.request.GetCommunityRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CommunityFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetCommunityResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QCommunityFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QFeedUserResponseDto;
import com.emelmujiro.secreto.feed.entity.FeedType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class FeedQueryRepository {

	@Value("${feed.page-size}")
	private int pageSize;

	private final JPAQueryFactory query;

	public FeedQueryRepository(EntityManager em) {
		this.query = new JPAQueryFactory(em);
	}

	public GetCommunityResponseDto getCommunity(GetCommunityRequestDto dto) {
		List<CommunityFeedResponseDto> content = query
			.select(new QCommunityFeedResponseDto(
				feed.id,
				feed.title,
				Expressions.asNumber(1), /* TODO: 하트 수 */
				feed.heartCount,
				feed.replyCount,
				Expressions.asString("value"),  /* TODO: 썸네일 이미지 */
				new QFeedUserResponseDto(
					feed.author.id,
					feed.author.nickname,
					feed.author.profileUrl
				),
				feed.createDate
			))
			.from(feed)
			.leftJoin(feed.author, user)
			.where(
				feed.feedType.eq(FeedType.COMMUNITY)
					.and(feed.deletedYn.eq(false))
					.and(stringContains(feed.title, dto.getKeyword())
						.or(stringContains(feed.author.nickname, dto.getKeyword()))
					)
			)
			.orderBy(feed.createDate.desc())
			.offset(dto.getOffset())
			.limit(pageSize + 1)
			.fetch();

		boolean hasNext = false;
		if (content.size() > pageSize) {
			content.remove(pageSize);
			hasNext = true;
		}
		return GetCommunityResponseDto.builder()
			.content(content)
			.offset(dto.getOffset() + pageSize)
			.hasNext(hasNext)
			.build();
	}

	private BooleanExpression stringContains(StringExpression expression, String pattern) {
		if (!StringUtils.hasText(pattern)) {
			return Expressions.TRUE;
		}
		if (expression == null) {
			return Expressions.FALSE;
		}
		return expression.lower().contains(pattern.toLowerCase());
	}
}
