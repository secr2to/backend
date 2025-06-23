package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetMyRoomUserRoleRequestDto {

    private Long roomId;

    private Long userId;
}
