package com.emelmujiro.secreto.chatting.dto.request;

import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateChattingReadStatusRequestDto {

    private long roomUserId;

    private List<Long> chattingMessageIds;

    @InjectPathVariable(name = "chattingRoomId")
    private long chattingRoomId;
}
