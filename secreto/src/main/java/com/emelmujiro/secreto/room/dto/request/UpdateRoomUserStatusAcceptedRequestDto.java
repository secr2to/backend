package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserStatusAcceptedRequestDto {

    private List<Long> roomUserIds;

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    @LoginUser
    private Long userId;
}
