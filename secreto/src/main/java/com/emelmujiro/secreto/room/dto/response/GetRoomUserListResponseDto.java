package com.emelmujiro.secreto.room.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomUserListResponseDto {

    private Long roomUserId;
    private Boolean managerYn;
    private Boolean standbyYn;
    private String nickname;
    private Boolean useProfileYn;
    private String selfIntroduction;
    private String profileUrl;
    private String roomCharacterUrl;
    private String searchId;
}
