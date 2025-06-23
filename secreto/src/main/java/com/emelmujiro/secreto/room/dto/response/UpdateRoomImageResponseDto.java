package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomImageResponseDto {

    private Long roomId;

    public static UpdateRoomImageResponseDto from(Room room) {

        return UpdateRoomImageResponseDto.builder()
                .roomId(room.getId())
                .build();
    }
}
