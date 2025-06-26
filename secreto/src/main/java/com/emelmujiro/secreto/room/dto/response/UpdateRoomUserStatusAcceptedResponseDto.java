package com.emelmujiro.secreto.room.dto.response;

import com.emelmujiro.secreto.room.entity.RoomUser;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserStatusAcceptedResponseDto {

    private Long roomUserId;

    public static List<UpdateRoomUserStatusAcceptedResponseDto> from(List<RoomUser> roomUserList) {

        return roomUserList.stream()
                .map(roomUser -> UpdateRoomUserStatusAcceptedResponseDto.builder()
                        .roomUserId(roomUser.getId())
                        .build()).toList();
    }
}
