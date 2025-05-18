package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.user.dto.response.UserProfileResponseDto;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomUserProfileResponseDto extends UserProfileResponseDto {

	private Long roomUserId;
	private String roomNickname;

	@QueryProjection
	public RoomUserProfileResponseDto(Long userId, String searchId, String profileUrl, Long roomUserId,
		String roomNickname) {
		super(userId, searchId, profileUrl);
		this.roomUserId = roomUserId;
		this.roomNickname = roomNickname;
	}
}
