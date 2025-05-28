package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserProfileResponseDto {

    private Long roomUserId;

    public static UpdateRoomUserProfileResponseDto from(RoomUser roomUser) {

        return UpdateRoomUserProfileResponseDto.builder()
                .roomUserId(roomUser.getId())
                .build();
    }
}
