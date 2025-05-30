package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomDetailsResponseDto {

    private Long roomId;

    public static UpdateRoomDetailsResponseDto from(Room room) {

        return UpdateRoomDetailsResponseDto.builder()
                .roomId(room.getId())
                .build();
    }
}
