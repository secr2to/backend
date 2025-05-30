package com.emelmujiro.secreto.chatting.dto.response;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipateType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingParticipationListResponseDto {

    private long roomUserId;

    private long chattingRoomId;

    private Enum<ChattingParticipateType> type;

    private LocalDateTime lastChattingDate;
}
