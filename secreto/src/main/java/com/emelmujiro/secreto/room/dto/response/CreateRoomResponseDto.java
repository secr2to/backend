package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateRoomResponseDto {

    private Long roomId;

    public static CreateRoomResponseDto from(Room room) {

        return CreateRoomResponseDto.builder()
                .roomId(room.getId())
                .build();
    }
}
