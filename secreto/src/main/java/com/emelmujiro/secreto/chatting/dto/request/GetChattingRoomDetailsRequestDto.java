package com.emelmujiro.secreto.chatting.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingRoomDetailsRequestDto {

    private Long roomId;
    private Long chattingRoomId;
    private Long userId;
}
