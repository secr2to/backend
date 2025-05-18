package com.emelmujiro.secreto.feed.repository;

import static com.emelmujiro.secreto.feed.entity.QFeed.*;
import static com.emelmujiro.secreto.room.entity.QRoomUser.*;
import static com.emelmujiro.secreto.user.entity.QUser.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.emelmujiro.secreto.feed.dto.request.GetCommunityRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetIngameFeedsRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CommunityFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetCommunityResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetIngameFeedsResponseDto;
import com.emelmujiro.secreto.feed.dto.response.IngameFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QCommunityFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QIngameFeedResponseDto;
import com.emelmujiro.secreto.feed.entity.FeedType;
import com.emelmujiro.secreto.room.dto.response.QRoomUserProfileResponseDto;
import com.emelmujiro.secreto.user.dto.response.QUserProfileResponseDto;
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

	public GetCommunityResponseDto findCommunityFeeds(GetCommunityRequestDto dto) {
		List<CommunityFeedResponseDto> content = query
			.select(new QCommunityFeedResponseDto(
				feed.id,
				feed.title,
				feed.imageCount,
				feed.heartCount,
				feed.replyCount,
				feed.thumbnailImage,
				new QUserProfileResponseDto(
					feed.author.id,
					feed.author.username,
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
						.or(stringContains(feed.author.searchId, dto.getKeyword()))
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
			.offset(hasNext ? dto.getOffset() + pageSize : -1)
			.hasNext(hasNext)
			.build();
	}

	public GetIngameFeedsResponseDto findIngameFeedsWithoutHeartsAndImages(GetIngameFeedsRequestDto dto) {
		List<IngameFeedResponseDto> content = query
			.select(new QIngameFeedResponseDto(
				feed.id,
				feed.title,
				feed.content,
				new QRoomUserProfileResponseDto(
					user.id,
					user.searchId,
					user.profileUrl,
					roomUser.id,
					roomUser.nickname
				),
				feed.replyCount,
				feed.createDate
			))
			.from(feed)
			.leftJoin(feed.author, user)
			.leftJoin(user.roomUserList, roomUser)
			.where(
				feed.deletedYn.eq(false),
				feed.feedType.eq(FeedType.INGAME),
				feed.room.id.eq(dto.getRoomId())
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
		return GetIngameFeedsResponseDto.builder()
			.content(content)
			.offset(hasNext ? dto.getOffset() + pageSize : -1)
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
