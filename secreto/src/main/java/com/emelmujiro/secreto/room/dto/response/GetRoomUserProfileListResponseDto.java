package com.emelmujiro.secreto.room.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomUserProfileListResponseDto {

    private Long roomUserId;
    private String profileUrl;
}
