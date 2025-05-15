package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomDetailsReqDto {

    private LocalDateTime endDate;
    private Integer missionPeriod;

    @Setter
    private long roomId;

    @Setter
    private long userId;
}
