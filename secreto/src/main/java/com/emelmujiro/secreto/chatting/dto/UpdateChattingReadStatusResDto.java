package com.emelmujiro.secreto.chatting.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateChattingReadStatusResDto {

    private long chattingMessageId;

    private boolean readYn;
}
