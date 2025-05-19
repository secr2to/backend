package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomDetailsRequestDto {

    private LocalDateTime endDate;
    private Integer missionPeriod;

    @InjectPathVariable(name = "roomId")
    private long roomId;

    @InjectUserId
    private long userId;
}
