package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomUserListRequestDto {

    private long userId;

    private long roomId;
}
