package com.emelmujiro.secreto.chatting.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChattingResDto {

    private long chattingMessageId;

    private long writerId;

    private String content;

    private LocalDateTime writeDate;

    private boolean readYn;

    private long chattingRoomId;
}
