package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomUserDetailsResponseDto {

    private Long roomUserId;
    private Boolean managerYn;
    private Boolean standbyYn;
    private String nickname;
    private String selfIntroduction;
    private String searchId;

    public static GetRoomUserDetailsResponseDto from(RoomUser roomUser) {

        return GetRoomUserDetailsResponseDto.builder()
                .roomUserId(roomUser.getId())
                .managerYn(roomUser.getManagerYn())
                .standbyYn(roomUser.getStandbyYn())
                .nickname(roomUser.getNickname())
                .selfIntroduction(roomUser.getSelfIntroduction())
                .searchId(roomUser.getUser().getSearchId())
                .build();
    }
}
