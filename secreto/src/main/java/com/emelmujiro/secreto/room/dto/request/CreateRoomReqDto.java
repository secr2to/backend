package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateRoomReqDto {

    private String name;
    private LocalDateTime endDate;
    private int missionPeriod;

    @Setter
    private long managerId;
}
