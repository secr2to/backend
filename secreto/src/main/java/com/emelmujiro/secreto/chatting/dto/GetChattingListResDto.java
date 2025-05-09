package com.emelmujiro.secreto.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingListResDto {

    private long chattingRoomId;

    private long writerId;

    private String content;

    private LocalDateTime writeDate;

    private boolean readYn;
}
