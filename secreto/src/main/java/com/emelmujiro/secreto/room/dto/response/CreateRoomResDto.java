package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateRoomResDto {

    private Long roomId;

    public static CreateRoomResDto from(Room room) {

        return CreateRoomResDto.builder()
                .roomId(room.getId())
                .build();
    }
}
