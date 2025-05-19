package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomStatusStartResponseDto {

    private Long roomId;

    public static UpdateRoomStatusStartResponseDto from(Room room) {

        return UpdateRoomStatusStartResponseDto.builder()
                .roomId(room.getId())
                .build();
    }
}
