package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.room.entity.RoomStatus;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomListReqDto {

    private RoomStatus status;

    private long userId;
}
