package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomDetailsResDto {

    private Long roomId;

    public static UpdateRoomDetailsResDto from(Room room) {

        return UpdateRoomDetailsResDto.builder()
                .roomId(room.getId())
                .build();
    }
}
