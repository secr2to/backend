package com.emelmujiro.secreto.mission.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomMissionListRequestDto {

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    @LoginUser
    private Long userId;
}
