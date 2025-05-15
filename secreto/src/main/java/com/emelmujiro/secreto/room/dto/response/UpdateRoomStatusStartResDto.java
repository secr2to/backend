package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomStatusStartResDto {

    private Long roomId;

    public static UpdateRoomStatusStartResDto from(Room room) {

        return UpdateRoomStatusStartResDto.builder()
                .roomId(room.getId())
                .build();
    }
}
