package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserSelfIntroductionRequestDto {

    private String selfIntroduction;

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    @InjectPathVariable(name = "roomUserId")
    private Long roomUserId;

    @LoginUser
    private Long userId;
}
