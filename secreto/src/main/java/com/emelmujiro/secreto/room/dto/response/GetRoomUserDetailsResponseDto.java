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
    private Boolean useProfileYn;
    private String selfIntroduction;
    private String profileUrl;
    private String roomCharacterUrl;

    public static GetRoomUserDetailsResponseDto from(RoomUser roomUser) {

        return GetRoomUserDetailsResponseDto.builder()
                .roomUserId(roomUser.getId())
                .managerYn(roomUser.getManagerYn())
                .standbyYn(roomUser.getStandbyYn())
                .nickname(roomUser.getNickname())
                .useProfileYn(roomUser.getUseProfileYn())
                .selfIntroduction(roomUser.getSelfIntroduction())
                .profileUrl(roomUser.getRoomProfile().getUrl())
                .roomCharacterUrl(roomUser.getRoomCharacter().getUrl())
                .build();
    }
}
