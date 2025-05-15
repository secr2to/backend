package com.emelmujiro.secreto.room.dto.request;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomStatusStartReqDto {

    private List<String> missionList;

    @Setter
    private long roomId;

    @Setter
    private long userId;
}
