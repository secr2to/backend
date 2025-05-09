package com.emelmujiro.secreto.chatting.dto;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipateType;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingParticipationListResDto {

    private long roomUserId;

    private long chattingRoomId;

    private Enum<ChattingParticipateType> type;
}
