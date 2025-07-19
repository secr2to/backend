package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetMyRoomUserDetailsResponseDto {

    private Long roomUserId;
    private Boolean managerYn;
    private Boolean standbyYn;
    private String nickname;
    private String selfIntroduction;
    private String profileUrl;
    private String searchId;
}