package com.emelmujiro.secreto.auth.dto;

import com.emelmujiro.secreto.user.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityContextUser {

	private Long userId;
	private String provider;
	private String role;
	private String email;
	private String username;
	private String nickname;
	private String profileUrl;

	public static SecurityContextUser of(User user) {
		return SecurityContextUser.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.username(user.getUsername())
			.nickname(user.getNickname())
			.profileUrl(user.getProfileUrl())
			.role("ROLE_USER") /* user 엔티티 권한 필드 추가 */
			.build();
	}
}
