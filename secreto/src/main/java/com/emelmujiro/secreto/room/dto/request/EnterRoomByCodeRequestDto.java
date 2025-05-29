package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnterRoomByCodeRequestDto {

    private String code;

    @LoginUser
    private Long userId;
}
