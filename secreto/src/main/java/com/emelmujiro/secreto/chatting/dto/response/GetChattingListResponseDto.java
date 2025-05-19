package com.emelmujiro.secreto.chatting.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingListResponseDto {

    private long chattingRoomId;

    private long writerId;

    private String content;

    private LocalDateTime writeDate;

    private boolean readYn;
}
