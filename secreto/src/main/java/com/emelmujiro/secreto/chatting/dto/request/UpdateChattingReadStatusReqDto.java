package com.emelmujiro.secreto.chatting.dto.request;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateChattingReadStatusReqDto {

    private long roomUserId;

    private List<Long> chattingMessageIds;

    @Setter
    private long chattingRoomId;
}
