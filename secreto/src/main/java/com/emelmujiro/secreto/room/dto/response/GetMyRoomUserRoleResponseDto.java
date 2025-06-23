package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetMyRoomUserRoleResponseDto {

    private Boolean isManagerYn;

    public static GetMyRoomUserRoleResponseDto from(RoomUser roomUser) {

        return GetMyRoomUserRoleResponseDto.builder()
                .isManagerYn(roomUser.getManagerYn())
                .build();
    }
}
