package com.emelmujiro.secreto.chatting.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChattingReqDto {

    private long writerId;

    private String content;

    @Setter
    private long chattingRoomId;

}
