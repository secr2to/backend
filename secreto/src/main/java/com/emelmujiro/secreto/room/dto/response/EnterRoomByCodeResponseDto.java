package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnterRoomByCodeResponseDto {

    private Long roomId;

    public static EnterRoomByCodeResponseDto from(Room room) {

        return EnterRoomByCodeResponseDto.builder()
                .roomId(room.getId())
                .build();
    }
}
