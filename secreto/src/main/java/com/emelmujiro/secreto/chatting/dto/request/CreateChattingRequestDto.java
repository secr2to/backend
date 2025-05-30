package com.emelmujiro.secreto.chatting.dto.request;

import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChattingRequestDto {

    private long writerId;

    private String content;

    @InjectPathVariable(name = "chattingRoomId")
    private Long chattingRoomId;

}
