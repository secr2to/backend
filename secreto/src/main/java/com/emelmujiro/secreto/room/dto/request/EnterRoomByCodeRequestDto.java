package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnterRoomByCodeRequestDto {

    private String code;

    @InjectUserId
    private Long userId;
}
