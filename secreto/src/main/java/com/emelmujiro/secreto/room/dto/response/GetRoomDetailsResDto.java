package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomDetailsResDto {

    private long roomId;
    private String name;
    private RoomStatus status;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int missionPeriod;

    public static GetRoomDetailsResDto from(Room room) {

        return GetRoomDetailsResDto.builder()
                .roomId(room.getId())
                .name(room.getName())
                .status(room.getRoomStatus())
                .code(room.getCode())
                .startDate(room.getStartDate())
                .endDate(room.getEndDate())
                .missionPeriod(room.getMissionPeriod())
                .build();
    }
}
