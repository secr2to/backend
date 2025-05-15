package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomUserListReqDto {

    private long userId;

    private long roomId;
}
