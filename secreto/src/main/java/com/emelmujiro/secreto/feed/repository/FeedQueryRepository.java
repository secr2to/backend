package com.emelmujiro.secreto.feed.repository;

import static com.emelmujiro.secreto.feed.entity.QFeed.*;
import static com.emelmujiro.secreto.room.entity.QRoomUser.*;
import static com.emelmujiro.secreto.user.entity.QUser.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.emelmujiro.secreto.feed.dto.request.GetIngameFeedsRequestDto;
import com.emelmujiro.secreto.feed.dto.response.GetIngameFeedsResponseDto;
import com.emelmujiro.secreto.feed.dto.response.IngameFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.QIngameFeedResponseDto;
import com.emelmujiro.secreto.feed.entity.FeedType;
import com.emelmujiro.secreto.room.dto.response.QRoomUserProfileResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class FeedQueryRepository {

	@Value("${feed.feed-paging-size}")
	private int pageSize;

	private final JPAQueryFactory query;

	public FeedQueryRepository(EntityManager em) {
		this.query = new JPAQueryFactory(em);
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
}
