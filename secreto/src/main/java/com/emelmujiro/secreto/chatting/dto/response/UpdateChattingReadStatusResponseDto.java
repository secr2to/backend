package com.emelmujiro.secreto.chatting.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateChattingReadStatusResponseDto {

    private long chattingMessageId;

    private boolean readYn;
}
