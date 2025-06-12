package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.room.entity.RoomStatus;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomListRequestDto {

    private RoomStatus status;

    private Long userId;
}
