package com.emelmujiro.secreto.chatting.dto.request;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipateType;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingListRequestDto {

    private Long roomId;

    private Long userId;

    private ChattingParticipateType type;
}
