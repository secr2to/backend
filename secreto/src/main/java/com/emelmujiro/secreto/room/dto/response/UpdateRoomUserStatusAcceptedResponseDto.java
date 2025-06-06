package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserStatusAcceptedResponseDto {

    private Long roomUserId;

    public static UpdateRoomUserStatusAcceptedResponseDto from(RoomUser roomUser) {

        return UpdateRoomUserStatusAcceptedResponseDto.builder()
                .roomUserId(roomUser.getId())
                .build();
    }
}
