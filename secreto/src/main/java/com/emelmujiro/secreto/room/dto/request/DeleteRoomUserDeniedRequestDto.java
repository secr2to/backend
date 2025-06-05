package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeleteRoomUserDeniedRequestDto {

    private Long userId;
    private Long roomId;
    private Long roomUserId;
}
