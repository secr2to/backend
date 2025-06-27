package com.emelmujiro.secreto.chatting.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChattingResponseDto {

    private Long chattingMessageId;

    private Long writerId;

    private String content;

    private LocalDateTime writeDate;

    private Boolean readYn;

    private Long chattingRoomId;
}
