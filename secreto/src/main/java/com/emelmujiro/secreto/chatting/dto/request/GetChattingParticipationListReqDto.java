package com.emelmujiro.secreto.chatting.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingParticipationListReqDto {

    private long roomId;

    private long userId;
}
