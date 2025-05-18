package com.emelmujiro.secreto.user.dto.response;

import com.emelmujiro.secreto.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserProfileResponseDto {

	private Long userId;
	private String searchId;
	private String profileUrl;

	@QueryProjection
	public UserProfileResponseDto(Long userId, String searchId, String profileUrl) {
		this.userId = userId;
		this.searchId = searchId;
		this.profileUrl = profileUrl;
	}

	public static UserProfileResponseDto from(User user) {
		return UserProfileResponseDto.builder()
			.userId(user.getId())
			.searchId(user.getSearchId())
			.profileUrl(user.getProfileUrl())
			.build();
	}
}
