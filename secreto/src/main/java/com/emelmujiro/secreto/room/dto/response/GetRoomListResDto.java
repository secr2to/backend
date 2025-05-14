package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomListResDto {

    private long roomId;
    private String name;
    private RoomStatus status;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int missionPeriod;
}
