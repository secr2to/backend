package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.dto.request.UpdateRoomUserSelfIntroductionRequestDto;
import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserSelfIntroductionResponseDto {

    private Long roomUserId;

    public static UpdateRoomUserSelfIntroductionResponseDto from(RoomUser roomUser) {

        return UpdateRoomUserSelfIntroductionResponseDto.builder()
                .roomUserId(roomUser.getId())
                .build();
    }
}
