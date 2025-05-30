package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateRoomUserProfileResponseDto {

    private Long roomUserId;

    public static CreateRoomUserProfileResponseDto from(RoomUser roomUser) {

        return CreateRoomUserProfileResponseDto.builder()
                .roomUserId(roomUser.getId())
                .build();
    }
}
